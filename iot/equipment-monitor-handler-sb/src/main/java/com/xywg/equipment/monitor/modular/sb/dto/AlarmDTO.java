package com.xywg.equipment.monitor.modular.sb.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:32 2018/8/28
 * Modified By : wangyifei
 */
@Data
public class AlarmDTO implements Serializable {
    String uuid;
    String deviceType;
    String deviceNo;
    Date deviceTime;
    String info;
}
