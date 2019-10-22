package com.xingyun.equipment.core.enums;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:24 2018/8/30
 * Modified By : wangyifei
 */
public enum FlagEnum {

    CRANE_7_HOURS_DATA(13,"CRANE_7_HOURS_DATA"),
    LIFT_7_HOURS_DATA(14,"LIFT_7_HOURS_DATA"),
    MONITOR_7_HOURS_DATA(18,"MONITOR_7_HOURS_DATA"),
    PM25_7_HOUR_DATA(7,"pm25"),
    PM10_7_HOUR_DATA(8,"pm10"),
    TEMPERATURE_7_HOUR_DATA(9,"temperature"),
     HUMIDITY_7_HOUR_DATA(10,"humidity"),
    WINDSPEED_7_HOUR_DATA(12,"wind_speed"),
    WINDDIRECTION_7_HOUR_DATA(11,"wind_direction")
    ;

    private int flag;
    private String key;

    FlagEnum(int flag,String key) {
        this.flag = flag;
        this.key = key;
    }

    public int getFlag() {
        return flag;
    }

    public String getKey() {
        return key;
    }

    public static int getFlag(String key){

        for( FlagEnum falg : FlagEnum.values() ){

            if(key.equals(falg.getKey())){
                return falg.flag;
            }

        }
        return -1;
    }


}
