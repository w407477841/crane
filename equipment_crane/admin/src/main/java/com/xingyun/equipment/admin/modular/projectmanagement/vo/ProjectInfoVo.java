package com.xingyun.equipment.admin.modular.projectmanagement.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xingyun.equipment.admin.modular.projectmanagement.model.ProjectInfo;
import lombok.Data;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/8/20
 *TIME:20:59
 */
@Data
public class ProjectInfoVo extends ProjectInfo {
    @TableField(exist = false)
    private String label;
    @TableField(exist = false)
    private Integer value;
    /**
     * 项目id
     */
    @TableField(exist = false)
    private Integer projectId;

    /**
     * 区域名称
     */
    @TableField(exist = false)
    private String positionName;

    /**
     * 施工单位名称
     */
    @TableField(exist = false)
    private String builderName;

    /**
     * 区域集合
     */
    @TableField(exist = false)
    private List<Integer> positions;

    /**
     * 施工单位集合
     */
    @TableField(exist = false)
    private List<Integer> builders;

    /**
     * 智慧工地项目id
     */
    @TableField(exist = false)
    private Integer smartProjectId;

    /**
     * 设备编号
     */
    @TableField(exist = false)
    private String deviceNo;

    /**
     * 类别
     */
    @TableField(exist = false)
    private String category;

    /**
     * 平台名称
     */
    @TableField(exist = false)
    private String applicationName;
}
