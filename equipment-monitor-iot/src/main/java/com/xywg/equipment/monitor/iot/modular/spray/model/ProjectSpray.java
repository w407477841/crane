package com.xywg.equipment.monitor.iot.modular.spray.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_project_spray")
public class ProjectSpray extends BaseEntity<ProjectSpray> {

    private static final long serialVersionUID = 1L;

    /**
     * 工程名称
     */
    @TableField("project_id")
    private Integer projectId;

    @TableField("device_no")
    private String deviceNo;

    @TableField("gprs")
    private String gprs;


    @TableField("identifier")
    private String identifier;


    @TableField("specification")
    private String specification;

    @TableField("owner")
    private String owner;

    @TableField("manufactor")
    private String manufactor;


    @TableField("is_online")
    private Integer isOnline;

    @TableField("status")
    private Integer status;

    @TableField("place_point")
    private String placePoint;


    @TableField("org_id")
    private Integer orgId;

    @TableField("name")
    private String name;

    private Double pm10;

    private Double pm25;



}
