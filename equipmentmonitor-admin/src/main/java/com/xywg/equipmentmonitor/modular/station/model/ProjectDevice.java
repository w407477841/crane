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
 * 安全帽
 * </p>
 *
 * @author hy
 * @since 2019-03-26
 */
@TableName("t_project_device")
@Data
public class ProjectDevice extends BaseEntity<ProjectDevice> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 设备号
     */
	@TableField("device_no")
	private String deviceNo;


	private Integer type;
    /**
     * 设备名称
     */
	private String name;
    /**
     * 工程id
     */
	@TableField("project_id")
	private Integer projectId;
    /**
     * 是否是最新绑定数据(0:当前绑定,1:代表是绑定履历数据)
     */
	@TableField("current_flag")
	private Integer currentFlag;
    /**
     * 启用状态:1启用,0未启用
     */
	private Integer status;
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
		return "ProjectDevice{" +
			"id=" + id +
			", name=" + name +
			", projectId=" + projectId +
			", currentFlag=" + currentFlag +
			", status=" + status +
			", comments=" + comments +
			"}";
	}
}
