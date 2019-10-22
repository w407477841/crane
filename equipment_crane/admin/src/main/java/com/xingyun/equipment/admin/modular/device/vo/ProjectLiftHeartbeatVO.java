package com.xingyun.equipment.admin.modular.device.vo;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xingyun.equipment.admin.modular.device.model.ProjectLiftHeartbeat;

import lombok.Data;

/***
 *@author:changmengyu
 *DATE:2018/8/22
 *TIME:10:39
 */@Data
public class ProjectLiftHeartbeatVO extends ProjectLiftHeartbeat {

	

    /**
     * 工程名称
     */
    @TableField(exist = false)
    private String projectName;
}
