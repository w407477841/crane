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
@TableName("t_project_device_worker_record")
@Data
public class ProjectDeviceWorkerRecord extends BaseEntity<ProjectDeviceWorkerRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 项目id
     */
	@TableField("project_id")
	private Integer projectId;
    /**
     * 组织结构
     */
	@TableField("org_id")
	private Integer orgId;
    /**
     * 设备id
     */
	@TableField("device_id")
	private Integer deviceId;
    /**
     * 设备编号
     */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 证件类型
     */
	@TableField("id_card_type")
	private Integer idCardType;
    /**
     * 证件编号
     */
	@TableField("id_card_number")
	private String idCardNumber;
    /**
     * 姓名
     */
	private String name;
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
		return "ProjectDeviceWorkerRecord{" +
			"id=" + id +
			", projectId=" + projectId +
			", orgId=" + orgId +
			", deviceId=" + deviceId +
			", deviceNo=" + deviceNo +
			", idCardType=" + idCardType +
			", idCardNumber=" + idCardNumber +
			", name=" + name +
			", comments=" + comments +
			"}";
	}
}
