package com.xingyun.equipment.crane.core.util;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:05 2019/4/17
 * Modified By : wangyifei
 */
public class KeyUtil {
    public static final String database = "station";
    public static final String table_master = "master";
    public static final String table_slave = "slave";
    public static final String table_map = "map";
    public static final String table_data_first = "data:first";
    public static final String table_data_second = "data:second";
    public static   String  key(String db,String tb,String pk,String pkv){
        StringBuffer sb = new StringBuffer();
        sb.append(db).append(":").append(tb).append(":").append(pk).append(":").append(pkv);
        return sb.toString();
    }

}
