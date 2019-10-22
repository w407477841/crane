package com.xywg.equipmentmonitor.modular.station.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.equipmentmonitor.core.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2019-03-20
 */
@Data
@TableName("t_project_accurate_position_data")
public class ProjectAccuratePositionData extends BaseEntity<ProjectAccuratePositionData> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 图片id
     */
	@TableField("map_id")
	private Integer mapId;
    /**
     * X轴（单位米）
     */
	@TableField("x_zhou")
	private BigDecimal xZhou;
    /**
     * Y轴（单位米）
     */
	@TableField("y_zhou")
	private BigDecimal yZhou;
    /**
     * 采集时间
     */
	@TableField("collect_time")
	private Date collectTime;
    /**
     * 备注
     */
	private String comments;

    /**
     * 标签号
     */
	@TableField("label_no")
	private String lableNo;
    /**
     * 人员信息
     */
	@TableField("identity_code")
	private String identityCode;
	@TableField(exist = false)
	private String name;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectAccuratePositionData{" +
			"id=" + id +
			", mapId=" + mapId +
			", xZhou=" + xZhou +
			", yZhou=" + yZhou +
			", collectTime=" + collectTime +
			", comments=" + comments +
			", lableNo=" + lableNo +
			", identityCode=" + identityCode +
			"}";
	}
}
