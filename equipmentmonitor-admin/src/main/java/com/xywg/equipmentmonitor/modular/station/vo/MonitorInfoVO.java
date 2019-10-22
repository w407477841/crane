package com.xywg.equipmentmonitor.modular.station.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:46 2019/3/28
 * Modified By : wangyifei
 */
@Data
public class MonitorInfoVO {

    private Boolean flag;
    private String deviceNo;
    private String value;
    private String unit;
    private Date time;

   public static MonitorInfoVO packageData(MonitorInfoVO info,Boolean flag,String deviceNo,String unit){

       info.setDeviceNo(deviceNo);
       info.setFlag(flag);
       info.setUnit(unit);
       return info;

   }

}
