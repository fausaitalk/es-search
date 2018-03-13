
package com.udgrp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kejw on 2017/8/16.
 */
public class Pager<E> implements Serializable {
    // 结果集
    private List<E> list;
    // 结果记录数
    private int count;
    // 每页多少条数据
    private int pageSize ;
    // 第几页
    private int pageNo;

    // 结果总页数
    public int getTotalPages() {
        return (count + pageSize - 1) / pageSize;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}