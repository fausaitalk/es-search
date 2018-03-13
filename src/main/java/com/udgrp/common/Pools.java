

package com.udgrp.common;


import com.udgrp.pools.base.PoolConfig;
import com.udgrp.pools.es.EsConnectionPool;
import com.udgrp.pools.hbase.HbaseConnectionPool;
import org.apache.hadoop.hbase.client.Connection;
import org.elasticsearch.client.transport.TransportClient;

/**
 * Created by kejw on 2017/6/16.
 */
abstract public class Pools {

    protected HbaseConnectionPool hbaseConnectionPool;

    protected EsConnectionPool esConnectionPool;

    public PoolConfig getPoolConfig() {
        PoolConfig poolConfig = null;
        if (poolConfig == null) {
            poolConfig = new PoolConfig();
            poolConfig.setMaxTotal(100);
            poolConfig.setMaxIdle(100);
            poolConfig.setMaxWaitMillis(1000000);
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);
            poolConfig.setTestOnCreate(true);
        }
        return poolConfig;
    }

    public synchronized Connection getHbaseConnection() {

        if (hbaseConnectionPool == null || hbaseConnectionPool.isClosed()) {
            hbaseConnectionPool = getHbaseConnectionPool();
        }

        return hbaseConnectionPool.getConnection();
    }

    public synchronized void returnHbaseConnection(Connection connection) {

        hbaseConnectionPool.returnConnection(connection);
    }

    public synchronized TransportClient getEsConnection() {

        if (esConnectionPool == null || esConnectionPool.isClosed()) {
            esConnectionPool = getEsConnectionPool();
        }
        return esConnectionPool.getConnection();

    }

    public synchronized void returnEsConnection(TransportClient connection) {

        esConnectionPool.returnConnection(connection);

    }

    abstract public HbaseConnectionPool getHbaseConnectionPool();

    abstract public EsConnectionPool getEsConnectionPool();
}


