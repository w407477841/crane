package com.xywg.equipmentmonitor.modular.remotesetting.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
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
 * @since 2019-01-18
 */
@TableName("t_project_application_config")
@Data
public class ProjectApplicationConfig extends BaseEntity<ProjectApplicationConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 平台名称
     */
	private String name;
    /**
     * topic
     */
	private String topic;

	private String comments;
    @TableField(value = "org_id",fill = FieldFill.INSERT)
	private Integer orgId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}


}
