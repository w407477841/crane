package com.xywg.equipmentmonitor.modular.device.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:43 2018/9/3
 * Modified By : wangyifei
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmInfoVO implements Serializable {

    Integer flag ;
    String  projectName;
    Integer projectId;
    String  alarmInfo;
    String deviceNo;
    Integer status;
    Integer id;
    String deviceTime;
    String value;
    String name;
    Integer deviceId;
    Integer detailId;
    Integer alarmId;


}
