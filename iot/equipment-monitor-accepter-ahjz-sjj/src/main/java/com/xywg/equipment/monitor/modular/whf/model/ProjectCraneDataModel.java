package com.xywg.equipment.monitor.modular.whf.model;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Data
public class ProjectCraneDataModel {


    /**
     * ID
     */
	private Integer id;
    /**
     * 重量
     */
	private String weight;
    /**
     * 力矩
     */
	private String moment;
	private String momentPercentage;
    /**
     * 高度
     */
	private String height;
    /**
     * 幅度
     */
	private String range;
    /**
     * 回转角度
     */
	private String rotaryAngle;
    /**
     * 风速
     */
	private String windSpeed;
	private String tiltAngle;
    /**
     * 删除标志
     */
	private Integer isDel;
    /**
     * 创建日期
     */
	private Date createTime;
    /**
     * 创建人
     */
	private Integer createUser;
    /**
     * 修改日期
     */
	private Date modifyTime;
    /**
     * 修改人
     */
	private Integer modifyUser;
    /**
     * 1：带标志位 2：不带标志位
     */
	private Integer flag;
	private String deviceNo;



}
