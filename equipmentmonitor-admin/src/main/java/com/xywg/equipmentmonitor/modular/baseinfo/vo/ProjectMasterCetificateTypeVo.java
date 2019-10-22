package com.xywg.equipmentmonitor.modular.baseinfo.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xywg.equipmentmonitor.modular.baseinfo.model.ProjectMasterCetificateType;
import lombok.Data;

/***
 *@author:jixiaojun
 *DATE:2018/9/4
 *TIME:15:23
 */
@Data
public class ProjectMasterCetificateTypeVo extends ProjectMasterCetificateType {
    @TableField(exist = false)
    private String label;
    @TableField(exist = false)
    private Integer value;
}
