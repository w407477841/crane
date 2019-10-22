package com.xywg.equipment.monitor.modular.whf.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@TableName("t_project_crane_alarm")
@Data
public class ProjectCraneAlarm extends DeviceAlarm<ProjectCraneAlarm> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 塔吊id
     */
	@TableField("crane_id")
	private Integer craneId;

	@TableField("detail_id")
	private Integer detailId;




	public static long getSerialVersionUID() {
		return serialVersionUID;
	}



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectCraneAlarm{" +
			"id=" + id +
			", craneId=" + craneId +

			"}";
	}
}
