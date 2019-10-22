package com.xingyun.equipment.crane.modular.device.vo;

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
    private String id;
    private String  projectName;
    private String  alarmInfo;
    private String craneId;
    private String craneNo;
    private String deviceNo;
    private Integer status;
    private String beginTime;
    private String endTime;
    private String modifyUserName;
    private String modifyTime;//处理意见
    private String comments;
    //是否处理
    private Integer isHandle;
    //处理时间
    private String alarmTime;
    //类型
    private Integer type;

}
