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
 * @since 2018-08-22
 */
@TableName("t_project_lift_alarm")
@Data
public class ProjectLiftAlarm extends DeviceAlarm<ProjectLiftAlarm> {

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


	@TableField("detail_id")
	private Integer detailId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjectLiftAlarm{" +
			"id=" + id +
			", liftId=" + liftId +
				"}";
	}
}
