package com.xingyun.equipment.admin.modular.infromation.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xingyun.equipment.admin.core.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yy
 * @since 2018-08-24
 */
@Data
@TableName("t_project_crane_data_model")
public class ProjectCraneDataModel extends BaseEntity<ProjectCraneDataModel> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
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
     * 司机
     */
	private String driver;
    /**
     * 时间
     */
	@TableField("device_time")
	private String deviceTime;
    /**
     * 状态
     */
	private String status;
    /**
     * 重量
     */
	private String weight;
    /**
     * 力矩
     */
	private String moment;
    /**
     * 高度
     */
	private String height;
    /**
     * 幅度
     */
	private String range;
    /**
     * 力矩百分比
     */
	@TableField("moment_percentage")
	private String momentPercentage;
    /**
     * 回转角度
     */
	@TableField("rotary_angle")
	private String rotaryAngle;
    /**
     * 倾斜角度
     */
	@TableField("tilt_angle")
	private String tiltAngle;
    /**
     * 风速
     */
	@TableField("wind_speed")
	private String windSpeed;
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
	 * 报警
	 */
	private String alarm;





	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
