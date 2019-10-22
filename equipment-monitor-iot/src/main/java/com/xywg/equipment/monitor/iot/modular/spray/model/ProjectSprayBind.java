package com.xywg.equipment.monitor.iot.modular.spray.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 */
@TableName("t_project_spray_bind")
@Data
public class ProjectSprayBind extends BaseEntity<ProjectSprayBind> {

    private static final long serialVersionUID = 1L;

	@TableField("spray_id")
	private Integer sprayId;

	@TableField("project_id")
	private Integer projectId;

	@TableField("environment_id")
	private Integer environmentId;

}
