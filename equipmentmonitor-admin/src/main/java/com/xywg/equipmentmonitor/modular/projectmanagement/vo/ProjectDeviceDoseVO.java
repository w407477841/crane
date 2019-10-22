package com.xywg.equipmentmonitor.modular.projectmanagement.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:39 2018/9/17
 * Modified By : wangyifei
 */
@Data
public class ProjectDeviceDoseVO implements Serializable {

    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * 设备电度数
     */
    private Float degree;
    /**
     * 用量
     */
    private Float used;

    /**
     * 设备抄表时间  yyyy-MM-dd
     */
    private String deviceTime;



}
