package com.xywg.equipment.monitor.modular.whf.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-22
 */
@TableName("t_project_lift_detail")
@Data
public class ProjectLiftDetail extends Model<ProjectLiftDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 升降机id
     */
	@TableField("lift_id")
	private Integer liftId;
    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 司机
     */
	private Integer driver;
    /**
     * 时间
     */
	@TableField("device_time")
	private Date deviceTime;
    /**
     * 重量
     */
	private Double weight;
    /**
     * 高度
     */
	private Double height;
    /**
     * 速度
     */
	private Double speed;
    /**
     * 人数
     */
	private Integer people;
    /**
     * 前门状态
     */
	@TableField("front_door_status")
	private Integer frontDoorStatus;
    /**
     * 后门状态
     */
	@TableField("back_door_status")
	private Integer backDoorStatus;
    /**
     * 升降机状态
     */
	private Integer status;

	@TableField(exist = false)
	private List<Integer> statusList;

    /**
     * 倾角
     */
	@TableField("tilt_angle")
	private Double tiltAngle;
    /**
     * 楼层
     */
	private Integer floor;
    /**
     * 启动楼层
     */
	@TableField("floor_start")
	private Integer floorStart;
    /**
     * 停止楼层
     */
	@TableField("floor_end")
	private Integer floorEnd;
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
	@TableField("is_del")
	private Integer isDel;
    /**
     * 创建日期
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_user")
	private Integer createUser;
    /**
     * 修改日期
     */
	@TableField("modify_time")
	private Date modifyTime;
    /**
     * 修改人
     */
	@TableField("modify_user")
	private Integer modifyUser;




	@Override
	protected Serializable pkVal() {
		return this.id;
	}


}
