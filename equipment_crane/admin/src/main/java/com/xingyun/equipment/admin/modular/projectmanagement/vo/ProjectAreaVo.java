package com.xingyun.equipment.admin.modular.projectmanagement.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xingyun.equipment.admin.modular.projectmanagement.model.ProjectArea;
import lombok.Data;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/9/4
 *TIME:11:28
 */
@Data
public class ProjectAreaVo extends ProjectArea {
    /**
     * 子集项目区域
     */
    @TableField(exist = false)
    private List<ProjectAreaVo> children;
}
