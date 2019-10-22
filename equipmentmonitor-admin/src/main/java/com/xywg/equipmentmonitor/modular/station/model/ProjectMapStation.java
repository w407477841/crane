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
@TableName("t_project_map_station")
@Data
public class ProjectMapStation extends BaseEntity<ProjectMapStation> {

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
     * 基站id（设备编号）
     */
	@TableField("station_id")
	private Integer stationId;
    /**
     * 基站号
     */
	@TableField("station_no")
	private String stationNo;
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
     * 备注
     */
	private String comments;




	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectMapStation{" +
			"id=" + id +
			", mapId=" + mapId +
			", stationId=" + stationId +
			", stationNo=" + stationNo +
			", xZhou=" + xZhou +
			", yZhou=" + yZhou +
			", comments=" + comments +
			"}";
	}
}
