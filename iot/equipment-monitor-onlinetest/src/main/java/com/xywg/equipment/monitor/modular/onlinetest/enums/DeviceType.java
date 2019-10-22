package com.xywg.equipment.monitor.modular.onlinetest.enums;

import com.xywg.equipment.monitor.modular.onlinetest.service.OnlineTestService;
import com.xywg.equipment.monitor.modular.onlinetest.service.impl.HfTdImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:59 2019/1/21
 * Modified By : wangyifei
 */
public enum DeviceType {
    HF_TD("crane","anhuijuzheng",HfTdImpl.class)
    ,HF_SJJ("lift","anhuijuzheng",HfTdImpl.class)
    ,DL_YC("monitor","daliantengyi",HfTdImpl.class)
    ,DL_SJJ("lift","daliantengyi",HfTdImpl.class)
    ,DL_TJ("crane","daliantengyi",HfTdImpl.class)
    ;
    /**
     *  设备类别
     */
    private String type;
    /**
     *  厂家
     */
    private String vender;
    private Class<? extends OnlineTestService> clazz;

    DeviceType(String type, String vender, Class<? extends OnlineTestService> clazz) {
        this.type = type;
        this.vender = vender;
        this.clazz = clazz;
    }

    public String getType() {
        return type;
    }

    public String getVender() {
        return vender;
    }

    public Class<? extends OnlineTestService> getClazz() {
        return clazz;
    }

    public static Class<? extends OnlineTestService>  getClazz(String vender, String type){

        for( DeviceType deviceType :DeviceType.values()){
            if(deviceType.getType().equals(type)&&deviceType.getVender().equals(vender)){
                return deviceType.getClazz();
            }
        }
            return null;
    }

}
