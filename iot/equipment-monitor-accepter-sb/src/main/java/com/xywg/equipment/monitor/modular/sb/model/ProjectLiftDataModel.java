package com.xywg.equipment.monitor.modular.sb.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-22
 */
@Data
public class ProjectLiftDataModel{


    /**
     * ID
     */
	private Integer id;
    /**
     * 规格型号
     */
	private String specification;
    /**
     * 制造厂家
     */
	private String manufactor;
    /**
     * 设备编号
     */
	private String deviceNo;
    /**
     * 司机
     */
	private String driver;
    /**
     * 时间
     */
	private String deviceTime;
    /**
     * 重量
     */
	private String weight;
    /**
     * 高度
     */
	private String height;
    /**
     * 速度
     */
	private String speed;
    /**
     * 人数
     */
	private String people;
    /**
     * 前门状态
     */
	private String frontDoorStatus;
    /**
     * 后门状态
     */
	private String backDoorStatus;
    /**
     * 升降机状态
     */
	private String status;
    /**
     * 楼层
     */
	private String floor;
    /**
     * 启动楼层
     */
	private String floorStart;
    /**
     * 停止楼层
     */
	private String floorEnd;
    /**
     * 倾角
     */
	private BigDecimal tiltAngle;
    /**
     * 预留的键值
     */
	private String key1;
    /**
     * 预留的键值
     */
	private String key2;
    /**
     * 预留的键值
     */
	private String key3;
    /**
     * 预留的键值
     */
	private String key4;
    /**
     * 预留的键值
     */
	private String key5;
    /**
     * 预留的键值
     */
	private String key6;
    /**
     * 预留的键值
     */
	private String key7;
    /**
     * 预留的键值
     */
	private String key8;
    /**
     * 预留的键值
     */
	private String key9;
    /**
     * 预留的键值
     */
	private String key10;
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



}
