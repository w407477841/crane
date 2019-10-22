package com.xywg.equipmentmonitor.modular.device.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.xywg.equipmentmonitor.core.model.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author yy
 * @since 2018-08-22
 */
@Data
@TableName("t_project_lift")
public class ProjectLift extends BaseEntity<ProjectLift> {

	private static final long serialVersionUID = 1L;


	/**
	 * ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	/**
	 * 工程名称
	 */
	@TableField("project_id")
	private Integer projectId;
	/**
	 * 产权编号
	 */
	private String identifier;
	/**
	 * 规格型号
	 */
	private String specification;
	/**
	 * 产权单位
	 */
	private String owner;
	/**
	 * 制造厂家
	 */
	private String manufactor;
	/**
	 * 最大载重量
	 */
	@TableField("max_weight")
	private BigDecimal maxWeight;
	/**
	 * 最大载人数
	 */
	@TableField("max_people")
	private Integer maxPeople;
	/**
	 * 标准高度
	 */
	@TableField("standard_height")
	private BigDecimal standardHeight;
	/**
	 * 运行速度
	 */
	private BigDecimal speed;
	/**
	 * 在线状态
	 */
	@TableField("is_online")
	private Integer isOnline;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 备注
	 */
	private String comments;
	/**
	 * 组织结构
	 */
	@TableField(value = "org_id",fill = FieldFill.INSERT)
	private Integer orgId;

	private Integer gprs;

	@TableField("place_point")
	private String placePoint;
	/**
	 * 设备编号
	 */
	@TableField("device_no")
	private String deviceNo;
	@ApiModelProperty(value = "名称")
	private String name ;

	/**
	 * 是否需要转发 1:需要 0:不需要
	 */
	@TableField("need_dispatch")
	private Integer needDispatch;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
