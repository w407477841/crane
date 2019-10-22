package com.xywg.equipment.monitor.iot.netty.device.dust;

/**
 * @author 严鹏
 * @date 2019/7/30
 */
public enum WindDirection{
    North("30","北风","000"),
    NorthEast("31","东北风","045"),
    East("32","东风","090"),
    SouthEast("33","东南风","135"),
    South("34","南风","180"),
    SouthWest("35","西南风","225"),
    West("36","西风","270"),
    NorthWest("37","西北风","315");
    String code;
    String name;
    String angle;
    WindDirection(String code, String name, String angle){
        this.code = code;
        this.name = name;
        this.angle = angle;
    }
}
