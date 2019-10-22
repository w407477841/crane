package com.xingyun.equipment.crane.modular.device.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.xingyun.equipment.core.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xss
 * @since 2018-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_crane_video")
public class ProjectCraneVideo extends BaseEntity<ProjectCraneVideo> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 塔吊id
     */
    @ApiModelProperty(value = "塔吊id")
    @TableField("crane_id")
    private Integer craneId;
    /**
     * 类别
     */
    private String type;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @TableField("login_name")
    private String loginName;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    @TableField("ip_address")
    private String ipAddress;
    /**
     * 端口号
     */
    @ApiModelProperty(value = "端口号")
    private String port;
    /**
     * 平台区分
     */
    @ApiModelProperty(value = "平台区分")
    @TableField("platform_type")
    private Integer platformType;
    /**
     * 通道
     */
    @ApiModelProperty(value = "通道")
    private Integer tunnel;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comments;
    /**
     * 组织结构
     */
    @ApiModelProperty(value = "组织结构")
    @TableField("org_id")
    private Integer orgId;

    /**
     * appkey
     */
    @ApiModelProperty(value = "appkey")
    private String appkey;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String secret;
    /**
     * 区域
     */
    @ApiModelProperty(value = "备注")
    private Integer area;

    /**
     * 乐橙视屏专用存储地址
     */
    @ApiModelProperty(value = "乐橙视屏专用存储地址")
    @TableField("http_address")
    private String httpAddress;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
