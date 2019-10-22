package com.xingyun.equipment.admin.modular.baseinfo.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xingyun.equipment.admin.modular.baseinfo.model.ProjectUser;
import lombok.Data;

/***
 *@author:jixiaojun
 *DATE:2018/8/28
 *TIME:13:47
 */
@Data
public class ProjectUserVo extends ProjectUser {
    /**
     * 项目名称
     */
    @TableField(exist = false)
    private String projectName;

    /**
     * 证书类型名称
     */
    @TableField(exist = false)
    private String certificateTypeName;
}
