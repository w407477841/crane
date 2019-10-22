package com.xywg.equipmentmonitor.modular.station.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
@TableName("t_project_map_floor")
@Data
public class ProjectMapFloor extends BaseEntity<ProjectMapFloor> {

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
     * 楼号楼层
     */
	private Integer floor;
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
		return "ProjectMapFloor{" +
			"id=" + id +
			", mapId=" + mapId +
			", floor=" + floor +
			", comments=" + comments +
			", isDel=" + getIsDel() +
			", createTime=" + getCreateTime() +
			", createUser=" + getCreateUser() +
			", modifyTime=" + getModifyTime() +
			", modifyUser=" + getModifyUser() +
			"}";
	}
}
