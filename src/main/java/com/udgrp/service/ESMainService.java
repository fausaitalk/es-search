

package com.udgrp.service;

import com.alibaba.fastjson.JSONObject;
import com.udgrp.bean.*;
import com.udgrp.common.PoolsImpl;
import com.udgrp.pools.es.ESConfig;
import com.udgrp.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by kejw on 2017/8/16.
 */
public class ESMainService extends PoolsImpl {
    private TransportClient esConn = null;
    private Connection hbaseConn = null;

    /**
     * 查询ES，根据得到的rowkey查询hbase
     * 根据车牌号码和时间查询
     *
     * @param plate          车牌号码
     * @param beginDate      开始日期
     * @param endDate        结束日期
     * @param pageNum        页码
     * @param pageSize       页大小
     * @param isFatigueDrive 是否疲劳驾驶
     * @return 结果json字符串
     */
    public String searchESSHW(String plate, String beginDate, String endDate, int pageNum, int pageSize, String isFatigueDrive) {
        Pager<ExListCarInfo> pageIndex = new Pager<ExListCarInfo>();
        System.out.println("start....");
        try {
            esConn = getEsConnection();
            hbaseConn = getHbaseConnection();
            BoolQueryBuilder qbool = QueryBuilders.boolQuery();
            if (StringUtils.isNotBlank(plate)) {
                qbool.must(QueryBuilders.termQuery("plate", plate));
            }
            if (StringUtils.isNotBlank(isFatigueDrive)) {
                qbool.filter(QueryBuilders.termQuery("isFatigueDrive", isFatigueDrive));
            }
            if (StringUtils.isNotBlank(beginDate)) {
                qbool.filter(QueryBuilders.rangeQuery("date").gte(beginDate));
            }
            if (StringUtils.isNotBlank(endDate)) {
                qbool.filter(QueryBuilders.rangeQuery("date").lte(endDate));
            }

            SearchResponse response = esConn.prepareSearch()
                    .setIndices(ESConfig.SHW_INDEX)
                    .setTypes(ESConfig.SHW_TYPE)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(qbool)
                    .addSort("date", SortOrder.ASC)//根据时间排序
                    .setFrom((pageNum - 1) * pageSize)// 从第几页开始搜
                    .setSize(pageSize) // 每页大小
                    .execute()
                    .actionGet();
            System.out.println("count=" + response.getHits().getHits().length);

            List<ExListCarInfo> list = new LinkedList<ExListCarInfo>();
            for (SearchHit rs : response.getHits().getHits()) {
                String rowKey = String.valueOf(rs.getSource().get("rowKey"));
                System.out.println("key==========================:" + rowKey);
                list.add(HbaseMainService.searchHbaseSHW(hbaseConn, rowKey));
            }
            long count = response.getHits().totalHits;
            pageIndex.setList(list);
            pageIndex.setCount(Integer.valueOf(String.valueOf(count)));// 所有数据总数
            pageIndex.setPageSize(pageSize);
            pageIndex.setPageNo(pageNum);

            System.out.println("end......");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询异常：" + e.getMessage());
        } finally {
            returnEsConnection(esConn);
            returnHbaseConnection(hbaseConn);
        }
        return JSONObject.toJSONString(pageIndex);
    }

    /**
     * 查询ES,针对汇总展示页面
     * 相同时间相同车牌只返回一条记录
     *
     * @param plate          车牌号码
     * @param beginDate      开始日期
     * @param endDate        结束日期
     * @param pageNum        页码
     * @param pageSize       页大小
     * @param isFatigueDrive 是否疲劳驾驶
     * @return 结果json字符串
     */
    public String searchES(String plate, String beginDate, String endDate, int pageNum, int pageSize, String isFatigueDrive) {
        System.out.println("start....");
        Pager<GPSIndex> pageIndex = new Pager<GPSIndex>();
        try {
            esConn = getEsConnection();
            BoolQueryBuilder qbool = QueryBuilders.boolQuery();
            if (StringUtils.isNotBlank(plate)) {
                qbool.must(QueryBuilders.termQuery("plate", plate));
            }
            if (StringUtils.isNotBlank(isFatigueDrive)) {
                qbool.filter(QueryBuilders.termQuery("isFatigueDrive", isFatigueDrive));
            }
            if (StringUtils.isNotBlank(beginDate)) {
                qbool.filter(QueryBuilders.rangeQuery("date").gte(beginDate));
            }
            if (StringUtils.isNotBlank(endDate)) {
                qbool.filter(QueryBuilders.rangeQuery("date").lte(endDate));
            }
            CollapseBuilder collapse = new CollapseBuilder("pd");
            SearchResponse response = esConn.prepareSearch()
                    .setIndices(ESConfig.SHW_INDEX)
                    .setTypes(ESConfig.SHW_TYPE).setQuery(qbool)
                    .setCollapse(collapse)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .addSort("date", SortOrder.ASC) //根据时间排序
                    .setFrom((pageNum - 1) * pageSize)// 从第几页开始搜
                    .setSize(pageSize)// 每页大小
                    .execute()
                    .actionGet();

            GPSIndex gpsIndex = null;

            List<GPSIndex> list = new LinkedList<GPSIndex>();
            System.out.println("total===>" + response.getHits().getHits().length);
            for (SearchHit rs : response.getHits().getHits()) {
                gpsIndex = new GPSIndex();
                String carPlate = String.valueOf(rs.getSource().get("plate"));
                String car_type = String.valueOf(rs.getSource().get("vehicleType"));
                String vehicleColor = String.valueOf(rs.getSource().get("vehicleColor"));
                String date = String.valueOf(rs.getSource().get("date"));
                gpsIndex.setPlate(carPlate);
                gpsIndex.setVehicleColor(vehicleColor);
                gpsIndex.setVehicleType(car_type);
                gpsIndex.setDate(date);
                list.add(gpsIndex);
            }
            long count = response.getHits().totalHits;
            pageIndex.setList(list);
            pageIndex.setCount(Integer.valueOf(String.valueOf(count)));// 所有数据总数
            pageIndex.setPageSize(pageSize);
            pageIndex.setPageNo(pageNum);
            System.out.println("end......");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询异常：" + e.getMessage());
        } finally {
            returnEsConnection(esConn);
        }
        return JSONObject.toJSONString(pageIndex);
    }

    /**
     * 不分页查询，专门针对GPS的方法
     *
     * @param plate     车牌号码
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param pageNum   页码
     * @param pageSize  页大小
     * @return 结果json字符串
     */
    public String searchESGPS(String plate, String beginDate, String endDate, int pageNum, int pageSize) {
        Pager<GPSRecordInfo> pageData = new Pager<GPSRecordInfo>();
        System.out.println("start....");
        try {
            esConn = getEsConnection();
            System.out.println("es end....");
            hbaseConn = getHbaseConnection();
            System.out.println("hbase end....");
            BoolQueryBuilder qbool = QueryBuilders.boolQuery();
            if (StringUtils.isNotBlank(plate)) {
                qbool.must(QueryBuilders.termQuery("plate", plate));
            }
            if (StringUtils.isNotBlank(beginDate)) {
                qbool.filter(QueryBuilders.rangeQuery("date").gte(DateUtil.getBeginDay(beginDate)));
            }
            if (StringUtils.isNotBlank(endDate)) {
                qbool.filter(QueryBuilders.rangeQuery("date").lte(DateUtil.getEndDay(endDate)));
            }
            SearchResponse response = esConn.prepareSearch()
                    .setIndices(ESConfig.GPS_INDEX)
                    .setTypes(ESConfig.GPS_TYPE).setQuery(qbool)
                    .addSort("date", SortOrder.ASC)//根据时间排序
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setSize(10000) // 每页大小
                    .setScroll(new TimeValue(60000))
                    .execute()
                    .actionGet();
            long count = response.getHits().totalHits;
            List<Get> rowKeys = null;
            LinkedList<GPSRecordInfo> result = new LinkedList<GPSRecordInfo>();
            do {
                rowKeys = new LinkedList<Get>();
                LinkedList<GPSRecordInfo> list = null;
                System.out.println("total===>" + response.getHits().getHits().length);
                for (SearchHit rs : response.getHits().getHits()) {
                    String rowKey = String.valueOf(rs.getSource().get("rowKey"));
                    //System.out.println("rowKey===>" + rowKey);
                    rowKeys.add(new Get(Bytes.toBytes(rowKey)));
                }
                list = HbaseMainService.searchHbaseGPS(hbaseConn, rowKeys);
                result.addAll(list);
                try {
                    response = esConn.prepareSearchScroll(response.getScrollId())
                            .setScroll(new TimeValue(60000)).execute().actionGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            while (response.getHits().getHits().length != 0);
            pageData.setList(result);
            pageData.setCount(Integer.valueOf(String.valueOf(count)));// 所有数据总数
            pageData.setPageSize(Integer.valueOf(String.valueOf(count)));
            pageData.setPageNo(1);
            System.out.println("end......");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询异常：" + e.getMessage());
        } finally {
            returnEsConnection(esConn);
            returnHbaseConnection(hbaseConn);
        }
        return JSONObject.toJSONString(pageData);
    }

    /**
     * 查询ES，根据得到的rowkey查询hbase
     * 根据车牌号码和时间查询
     *
     * @param plate     车牌号码
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param pageNum   页码
     * @param pageSize  页大小
     * @return 结果json字符串
     */
    public String searchESGPSMISS(String plate, String beginDate, String endDate, int pageNum, int pageSize) {
        Pager<GPSMissInfo> pageIndex = new Pager<GPSMissInfo>();
        System.out.println("start....");
        try {
            esConn = getEsConnection();
            hbaseConn = getHbaseConnection();
            BoolQueryBuilder qbool = QueryBuilders.boolQuery();
            if (StringUtils.isNotBlank(plate)) {
                qbool.must(QueryBuilders.termQuery("plate", plate));
            }
            if (StringUtils.isNotBlank(beginDate)) {
                qbool.filter(QueryBuilders.rangeQuery("nextTime").gte(DateUtil.getBeginDay(beginDate)));
            }
            if (StringUtils.isNotBlank(endDate)) {
                qbool.filter(QueryBuilders.rangeQuery("nextTime").lte(DateUtil.getEndDay(endDate)));
            }
            SearchResponse response = esConn.prepareSearch()
                    .setIndices(ESConfig.GPS_MISS_INDEX)
                    .setTypes(ESConfig.GPS_MISS_TYPY)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(qbool)
                    .addSort("nextTime", SortOrder.ASC)//根据时间排序
                    .setFrom((pageNum - 1) * pageSize)// 从第几页开始搜
                    .setSize(pageSize) // 每页大小
                    .setScroll(new TimeValue(60000))
                    .execute()
                    .actionGet();

            List<GPSMissInfo> list = new LinkedList<GPSMissInfo>();
            for (SearchHit rs : response.getHits().getHits()) {
                String rowKey = String.valueOf(rs.getSource().get("rowKey"));
                System.out.println("key==========================:" + rowKey);
                list.add(HbaseMainService.searchHbaseGPSMiss(hbaseConn, rowKey));
            }
            long count = response.getHits().totalHits;
            pageIndex.setList(list);
            pageIndex.setCount(Integer.valueOf(String.valueOf(count)));// 所有数据总数
            pageIndex.setPageSize(pageSize);
            pageIndex.setPageNo(pageNum);

            System.out.println("end......");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询异常：" + e.getMessage());
        } finally {
            returnEsConnection(esConn);
            returnHbaseConnection(hbaseConn);
        }
        return JSONObject.toJSONString(pageIndex);
    }

    /**
     * 统计车辆出入城市次数
     *
     * @param plate
     * @param beginDate
     * @param endDate
     * @param size
     * @return {"江门市":7,"阳江市":25,"广州市":36}
     */
    public String searchES_City(String plate, String beginDate, String endDate, int size) {
        System.out.println("start....");
        Map<String, Long> map = new HashMap<String, Long>();
        String arr[] = {"enCity", "exCity"};
        for (int i = 0; i < arr.length; i++) { //循环分别查出和入城市次数
            try {
                esConn = getEsConnection();
                BoolQueryBuilder qbool = QueryBuilders.boolQuery();
                if (StringUtils.isNotBlank(plate)) {
                    qbool.must(QueryBuilders.termQuery("plate", plate));
                }
                if (StringUtils.isNotBlank(beginDate)) {
                    qbool.filter(QueryBuilders.rangeQuery("date").gte(beginDate));
                }
                if (StringUtils.isNotBlank(endDate)) {
                    qbool.filter(QueryBuilders.rangeQuery("date").lte(endDate));
                }
                AggregationBuilder aggregationBuilder = AggregationBuilders
                        .terms(arr[i]).field(arr[i]).size(size);
                SearchResponse response = esConn.prepareSearch()
                        .setIndices(ESConfig.SHW_INDEX)
                        .setTypes(ESConfig.SHW_TYPE).setQuery(qbool)
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        .addAggregation(aggregationBuilder)
                        .setSize(0)
                        .execute()
                        .actionGet();

                Terms genders = response.getAggregations().get(arr[i]);
                System.out.println("total===>" + response.getHits().getHits().length);
                for (Terms.Bucket entry : genders.getBuckets()) {
                    String cityName = (String) entry.getKey();
                    if (StringUtils.isBlank(cityName)) {
                        continue;
                    }
                    long count = entry.getDocCount();
                    System.out.println("cityName:" + cityName + "-----" + "count:" + count);
                    if (map.get(cityName) != null) {
                        map.put(cityName, map.get(cityName) + count);
                    } else {
                        map.put(cityName, count);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("查询异常：" + e.getMessage());
            } finally {
                returnEsConnection(esConn);
            }
        }
        return JSONObject.toJSONString(map);
    }

    /**
     * 同城非同城统计 1同城0非同城
     *
     * @param plate
     * @param beginDate
     * @param endDate
     * @return {"0":65657,"1":73886}
     */
    public String searchES_Scope(String plate, String beginDate, String endDate) {
        System.out.println("start....");
        Map<String, Long> map = new HashMap<String, Long>();
        try {
            esConn = getEsConnection();
            BoolQueryBuilder qbool = QueryBuilders.boolQuery();
            if (StringUtils.isNotBlank(plate)) {
                qbool.must(QueryBuilders.termQuery("plate", plate));
            }
            if (StringUtils.isNotBlank(beginDate)) {
                qbool.filter(QueryBuilders.rangeQuery("date").gte(beginDate));
            }
            if (StringUtils.isNotBlank(endDate)) {
                qbool.filter(QueryBuilders.rangeQuery("date").lte(endDate));
            }
            AggregationBuilder aggregationBuilder = AggregationBuilders
                    .terms("isSameCity").field("isSameCity");
            SearchResponse response = esConn.prepareSearch()
                    .setIndices(ESConfig.SHW_INDEX)
                    .setTypes(ESConfig.SHW_TYPE).setQuery(qbool)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .addAggregation(aggregationBuilder)
                    .setSize(0)
                    .execute()
                    .actionGet();

            Terms genders = response.getAggregations().get("isSameCity");
            System.out.println("total===>" + response.getHits().getHits().length);
            for (Terms.Bucket entry : genders.getBuckets()) {
                String key = (String) entry.getKey();
                if (StringUtils.isBlank(key)) {
                    continue;
                }
                long count = entry.getDocCount();
                System.out.println("key:" + key + "-----" + "count:" + count);
                map.put(key, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询异常：" + e.getMessage());
        } finally {
            returnEsConnection(esConn);
        }
        return JSONObject.toJSONString(map);
    }

    /**
     * 查询车辆有上下高速记录，GPS有中断记录数据
     * 查询ES，根据得到的rowkey查询hbase
     * 根据车牌号码和时间查询
     *
     * @param plate     车牌号码
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param pageNum   页码
     * @param pageSize  页大小
     * @return 结果json字符串
     */
    public String searchESGPSMISS_2(String plate, String beginDate, String endDate, int pageNum, int pageSize) {
        Pager<GPSMissInfo> pageIndex = new Pager<GPSMissInfo>();

        long two = DateUtil.getTime2InMillis(endDate);
        long five = DateUtil.getTime5InMillis(endDate);

        System.out.println("start....");
        try {
            esConn = getEsConnection();
            hbaseConn = getHbaseConnection();
            BoolQueryBuilder qbool = QueryBuilders.boolQuery();
            if (StringUtils.isNotBlank(plate)) {
                qbool.must(QueryBuilders.termQuery("plate", plate));
            }
            if (StringUtils.isNotBlank(beginDate)) {
                qbool.filter(QueryBuilders.rangeQuery("nextTime").gte(DateUtil.getBeginDay(beginDate)));
            }
            if (StringUtils.isNotBlank(endDate)) {
                qbool.filter(QueryBuilders.rangeQuery("nextTime").lte(DateUtil.getEndDay(endDate)));
            }
            SearchResponse response = esConn.prepareSearch()
                    .setIndices(ESConfig.GPS_MISS_INDEX)
                    .setTypes(ESConfig.GPS_MISS_TYPY)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(qbool)
                    .addSort("nextTime", SortOrder.ASC)//根据时间排序
                    .setFrom((pageNum - 1) * pageSize)// 从第几页开始搜
                    .setSize(pageSize) // 每页大小
                    .setScroll(new TimeValue(60000))
                    .execute()
                    .actionGet();

            List<GPSMissInfo> list = new LinkedList<GPSMissInfo>();
            for (SearchHit rs : response.getHits().getHits()) {
                String rowKey = String.valueOf(rs.getSource().get("rowKey"));
                long nextTimes = DateUtil.getTimeInMillis(String.valueOf(rs.getSource().get("nextTime")));
                long preTimes = DateUtil.getTimeInMillis(String.valueOf(rs.getSource().get("preTime")));
                if ((two >= preTimes && two <= nextTimes) ||
                        (five >= preTimes && five <= nextTimes) ||
                        (two >= preTimes && five <= nextTimes) ||
                        (preTimes >= two && nextTimes <= five)) {
                    System.out.println("key==========================:" + rowKey);
                    list.add(HbaseMainService.searchHbaseGPSMiss(hbaseConn, rowKey));
                }
            }
            long count = response.getHits().totalHits;
            pageIndex.setList(list);
            pageIndex.setCount(Integer.valueOf(String.valueOf(count)));// 所有数据总数
            pageIndex.setPageSize(pageSize);
            pageIndex.setPageNo(pageNum);

            System.out.println("end......");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询异常：" + e.getMessage());
        } finally {
            returnEsConnection(esConn);
            returnHbaseConnection(hbaseConn);
        }
        return JSONObject.toJSONString(pageIndex);
    }
}