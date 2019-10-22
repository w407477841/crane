package com.xywg.equipment.sandplay.core.enums;

import com.xywg.equipment.sandplay.config.properties.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:00 2018/9/26
 * Modified By : wangyifei
 */
public enum DeviceType {

    LIFT("lift","升降机",CmdLift.class),
    CRANE("crane","塔吊机",CmdCrane.class),
    GATE("gate","闸机",CmdGate.class),
    LAMP("lamp","太阳能路灯",CmdLamp.class),
    WEIGHT("weight","电子称重",CmdWeight.class),
    SPRAY("spray","喷水系统",CmdSpray.class)

        ;

    DeviceType(String type, String name,Class<? extends BaseCmd> clazz) {
        this.type = type;
        this.name = name;
        this.clazz =  clazz;

    }

    private String type ;
    private String name;
    private Class<? extends BaseCmd> clazz;

    public Class<? extends BaseCmd> getClazz() {
        return clazz;
    }

    /**
     *
     * @param type
     * @return
     */
    public static DeviceType  getByType(String type){
        if(type!=null) {
            for (DeviceType deviceType : DeviceType.values()) {
                if (type.equals(deviceType.type)) {
                    return deviceType;
                }
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
