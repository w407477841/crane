package com.xingyun.equipment.crane.modular.remotesetting.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.xingyun.equipment.core.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;



import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xss
 * @since 2018-09-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_device_upgrade_package")
public class ProjectDeviceUpgradePackage extends BaseEntity<ProjectDeviceUpgradePackage> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 升级包名
     */
    @ApiModelProperty(value = "升级包名")
    private String name;
    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;
    /**
     * 路径
     */
    @ApiModelProperty(value = "路径")
    private String path;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comments;


    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
