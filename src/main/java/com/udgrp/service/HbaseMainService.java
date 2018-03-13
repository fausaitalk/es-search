

package com.udgrp.service;

import com.udgrp.bean.GPSMissInfo;
import com.udgrp.common.PoolsImpl;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import com.udgrp.bean.ExListCarInfo;
import com.udgrp.bean.GPSRecordInfo;
import com.udgrp.pools.hbase.HbaseConfig;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kejw on 2017/8/16.
 */
public class HbaseMainService extends PoolsImpl {

    public static LinkedList<GPSRecordInfo> searchHbaseGPS(Connection hbaseConn,  List<Get> rowKeys) {

        LinkedList<GPSRecordInfo> GPSRecordInfoList = new LinkedList<GPSRecordInfo>();
        Table ht = null;
        try {
            ht = hbaseConn.getTable(TableName.valueOf(HbaseConfig.TABLE_GPS__NAME));
            Result results[] = ht.get(rowKeys);
            for (Result result : results)
            {
                GPSRecordInfo gpsRecordInfo = new GPSRecordInfo();
                String plate = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "plate".getBytes()));
                String vehicleColor = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "vehicleColor".getBytes()));
                String vehicleType = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "vehicleType".getBytes()));
                String GPSTime = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "GPSTime".getBytes()));
                String lon = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "lon".getBytes()));
                String lat = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "lat".getBytes()));
                String vec1 = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "vec1".getBytes()));
                String vec2 = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "vec2".getBytes()));
                String vec3 = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "vec3".getBytes()));
                String direction = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "direction".getBytes()));
                String altitude = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "altitude".getBytes()));
                String state = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "state".getBytes()));
                String alarm = Bytes.toString(result.getValue("gpsdatainfo".getBytes(), "alarm".getBytes()));

                gpsRecordInfo.setPlate(plate);
                gpsRecordInfo.setVehicleColor(vehicleColor);
                gpsRecordInfo.setVehicleType(vehicleType);
                gpsRecordInfo.setGPSTime(GPSTime);
                gpsRecordInfo.setLon(lon);
                gpsRecordInfo.setLat(lat);
                gpsRecordInfo.setVec1(vec1);
                gpsRecordInfo.setVec2(vec2);
                gpsRecordInfo.setVec3(vec3);
                gpsRecordInfo.setDirection(direction);
                gpsRecordInfo.setAlarm(alarm);
                gpsRecordInfo.setAltitude(altitude);
                gpsRecordInfo.setState(state);

                GPSRecordInfoList.add(gpsRecordInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return GPSRecordInfoList;
    }

    public static ExListCarInfo searchHbaseSHW(Connection hbaseConn, String rowKey) {
        ExListCarInfo exListCarInfo = new ExListCarInfo();
        Table ht = null;
        try {
            ht = hbaseConn.getTable(TableName.valueOf(HbaseConfig.TABLE_SHW_NAME));
            Get get = new Get(rowKey.getBytes());
            Result result = ht.get(get);
            exListCarInfo.setPlate(Bytes.toString(result.getValue("exlistinfo".getBytes(), "plate".getBytes())));
            exListCarInfo.setVehicleColor(Bytes.toString(result.getValue("exlistinfo".getBytes(), "vehicleColor".getBytes())));
            exListCarInfo.setVehicleType(Bytes.toString(result.getValue("exlistinfo".getBytes(), "vehicleType".getBytes())));
            exListCarInfo.setEnTime(Bytes.toString(result.getValue("exlistinfo".getBytes(), "enTime".getBytes())));
            exListCarInfo.setExTime(Bytes.toString(result.getValue("exlistinfo".getBytes(), "exTime".getBytes())));
            exListCarInfo.setEnloadNetName(Bytes.toString(result.getValue("exlistinfo".getBytes(), "enloadNetName".getBytes())));
            exListCarInfo.setEnLoadName(Bytes.toString(result.getValue("exlistinfo".getBytes(), "enLoadName".getBytes())));
            exListCarInfo.setEnStationName(Bytes.toString(result.getValue("exlistinfo".getBytes(), "enStationName".getBytes())));
            exListCarInfo.setExloadNetName(Bytes.toString(result.getValue("exlistinfo".getBytes(), "exloadNetName".getBytes())));
            exListCarInfo.setExLoadName(Bytes.toString(result.getValue("exlistinfo".getBytes(), "exLoadName".getBytes())));
            exListCarInfo.setExStationName(Bytes.toString(result.getValue("exlistinfo".getBytes(), "exStationName".getBytes())));
            exListCarInfo.setEnlon(Bytes.toString(result.getValue("exlistinfo".getBytes(), "enlon".getBytes())));
            exListCarInfo.setEnlat(Bytes.toString(result.getValue("exlistinfo".getBytes(), "enlat".getBytes())));
            exListCarInfo.setExlon(Bytes.toString(result.getValue("exlistinfo".getBytes(), "exlon".getBytes())));
            exListCarInfo.setExlat(Bytes.toString(result.getValue("exlistinfo".getBytes(), "exlat".getBytes())));
            exListCarInfo.setEnCity(Bytes.toString(result.getValue("exlistinfo".getBytes(), "enCity".getBytes())));
            exListCarInfo.setExCity(Bytes.toString(result.getValue("exlistinfo".getBytes(), "exCity".getBytes())));
            exListCarInfo.setMidstations(Bytes.toString(result.getValue("exlistinfo".getBytes(), "midstations".getBytes())));
            exListCarInfo.setMinutes(Bytes.toString(result.getValue("exlistinfo".getBytes(), "minutes".getBytes())));
            exListCarInfo.setKm(Bytes.toString(result.getValue("exlistinfo".getBytes(), "km".getBytes())));
            exListCarInfo.setPreSpeed(Bytes.toString(result.getValue("exlistinfo".getBytes(), "preSpeed".getBytes())));
            exListCarInfo.setIsFatigueDrive(Bytes.toString(result.getValue("exlistinfo".getBytes(), "isFatigueDrive".getBytes())));

            System.out.println("enLaneId:"+Bytes.toString(result.getValue("exlistinfo".getBytes(), "enLaneId".getBytes())));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return exListCarInfo;
    }

    public static GPSMissInfo searchHbaseGPSMiss(Connection hbaseConn, String rowKey) {
        GPSMissInfo gpsMissInfo = new GPSMissInfo();
        Table ht = null;
        try {
            ht = hbaseConn.getTable(TableName.valueOf(HbaseConfig.TABLE_GPS_MISS_NAME));
            Get get = new Get(rowKey.getBytes());
            Result result = ht.get(get);
            gpsMissInfo.setPlate(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "plate".getBytes())));
            gpsMissInfo.setVehicleColor(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "vehicleColor".getBytes())));
            gpsMissInfo.setVehicleType(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "vehicleType ".getBytes())));
            gpsMissInfo.setPreTime(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "preTime".getBytes())));
            gpsMissInfo.setNextTime(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "nextTime".getBytes())));
            gpsMissInfo.setPreLon(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "preLon".getBytes())));
            gpsMissInfo.setPreLat(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "preLat".getBytes())));
            gpsMissInfo.setNextLon(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "nextLon".getBytes())));
            gpsMissInfo.setNextLat(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "nextLat".getBytes())));
            gpsMissInfo.setPreLoc(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "preLoc".getBytes())));
            gpsMissInfo.setNextLoc(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "nextLoc".getBytes())));
            gpsMissInfo.setDistance(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "distance".getBytes())));
            gpsMissInfo.setDuration(Bytes.toString(result.getValue("gpsrecordmiss".getBytes(), "duration".getBytes())));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return gpsMissInfo;
    }
}
