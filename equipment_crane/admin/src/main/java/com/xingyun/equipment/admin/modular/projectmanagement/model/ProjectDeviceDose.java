package com.xingyun.equipment.admin.modular.projectmanagement.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hjy
 */
@Data
public class ProjectDeviceDose implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer page;

    private Integer start;
    private Integer end;

    /**
     * 项目uuid
     */
    private String uuid;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 查询时间
     */
    private String queryTime;
    /**
     * 查询时间前一天
     */
    private String frontQueryTime;
    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * 设备电度数
     */
    private Float degree;
    /**
     * 设备抄表时间  yyyy-MM-dd
     */
    private String deviceTime;

    /**
     * 查询的年月
     */
    private String yearMonth;

}
