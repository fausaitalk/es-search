

package com.udgrp.pools.es;

/**
 * Created by kejw on 2017/6/16.
 */
public interface ESConfig {

    public static final String DEFAULT_HOST = "localhost";

    public static final String DEFAULT_PORT = "9300";

    public static final String CLUSTER_NAME = "Es_Cluster";

    public static final String URL = "elasticsearch://localhost:9300";

    public static final String GPS_INDEX = "jtt_gps";
    public static final String GPS_TYPE = "jtt_gps";

    public static final String SHW_INDEX = "jtt_shw";
    public static final String SHW_TYPE = "jtt_shw";

    public static final String GPS_MISS_INDEX = "jtt_gps_miss";
    public static final String GPS_MISS_TYPY = "jtt_gps_miss";
}
