

package com.udgrp.common;

import java.io.Serializable;

/**
 * Created by kejw on 2017/6/16.
 */
public class Constants implements Serializable {
    public static final String APP_NAME = "DRIVER";

    public static final long DURATION = 1000L;

    public static final String CHECKPOINT = "./checkpiont_trafficwrite";


    public static final String KAFKA_SERVER = "datanode1:6667,datanode2:6667,datanode3:6667";

    public static final String KAFKA_TOPIC = "traffic";

    public static final String KAFKA_KEY_DESERIALIZER = "org.apache.kafka.common.serialization.ByteArrayDeserializer";

    public static final String KAFKA_VALUE_DESERIALIZER = "org.apache.kafka.common.serialization.ByteArrayDeserializer";

    public static final String KAFKA_KEY_SERIALIZER = "org.apache.kafka.common.serialization.ByteArraySerializer";

    public static final String KAFKA_VALUE_SERIALIZER = "org.apache.kafka.common.serialization.ByteArraySerializer";

    public static final String KAFKA_GROUPID = "traffic_group";

    public static final String KAFKA_AUTO_OFFSET_RESET = "latest";

    public static final String KAFKA_ENABLE_AUTO_COMMIT = "true";

    public static final String KAFKA_PRODUCER_TYPE = "async";

    public static final String KAFKA_BATCH_NUM_MESSAGES = "1000";

    public static final String KAFKA_MAX_REQUEST_SIZE = "1000973460";


    public static final String ALLUXIO_PATH = "/APP/SOLAR/";

    public static final String KAFKA_ILLEGAL_TOPIC = "Illegal";

    public static final String KAFKA_ALARM_TOPIC = "";


    public static final String HBASE_TABLE_BASEINFO = "BaseInfo";

    public static final String HBASE_ZOOKEEPER_QUORUM = "datanode1.com,namenode1.com,namenode2.com";

    public static final String HBASE_ZOOKEEPER_PROPERTY_CLIENTPOINT = "2181";

    public static final String HBASE_CLIENT_WRITE_BUFFER = "12582912";

    public static final String HBASE_CLEINT_MAX_TOTAL_TASKS = "500";

    public static final String HBASE_CLIENT_MAX_PRESERVER_TASKS = "100";

    public static final String HABSE_CLIENT_MAX_PERREGION_TASKS = "20";

    public static final String HBASE_TABLE_STATISTICS = "TrafficInfo";

    public static final String STATISTICS_FAMILY_NAME = "trafficinfo";

    public static final String ZOOKEEPER_ZNODE_PARENT = "/hbase-unsecure";

    public static final String HBASE_TABLE_ILLEGALINFO = "";

    public static final String HBASE_ILLEGALINFO_COLUMNFAMILY = "";


    public static final String ES_CLUSTER_NAME = "my-application";


    public static final String ES_URL = "192.168.2.221:9300,192.168.2.221:9301";
    //public static final String ES_URL = "datanode1:9300,datanode2:9300,datanode3:9300";


    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    public static final String MYSQL_USER_NAME = "root";

    public static final String MYSQL_USER_PASSWORD = "mysql";

    public static final String MYSQL_JDBC_URL = "jdbc:mysql://172.20.31.127:3306/solar";


    public static final String REDIS_HOST = "DataStore";

    public static final String REDIS_PORT = "6379";
}
