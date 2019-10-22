package com.xywg.equipmentmonitor.modular.device.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xywg.equipmentmonitor.core.model.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 15:07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_spray_bind")
public class ProjectSprayBind extends BaseEntity<ProjectSprayBind>{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 工程名称
     */
    @ApiModelProperty(value = "工程名称")
    @TableField("project_id")
    private Integer projectId;
    /**
     * 喷淋
     */
    @ApiModelProperty(value = "产权编号")
    private Integer sprayId;
    /**
     * 扬尘
     */
    @ApiModelProperty(value = "规格型号")
    private Integer environmentId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
