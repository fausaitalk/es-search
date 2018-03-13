


package com.udgrp.pools.base;

import java.io.Serializable;

/**
 * Created by kejw on 2017/6/16.
 */
public interface ConnectionPool<T> extends Serializable {


    public abstract T getConnection();

    public void returnConnection(T conn);


    public void invalidateConnection(T conn);
}
