package com.xingyun.equipment.crane.modular.device.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xingyun.equipment.crane.modular.device.model.ProjectHelmet;
import lombok.Data;

import java.util.List;

/**
 * @author hjy
 * @date 2018/12/6
 */
@Data
public class ProjectTransfersDTO {

    private List<ProjectHelmet> listData;
    /**
     * 备注
     */
    private String comments;

    /**
     * 工程id
     */
    @TableField("project_id")
    private Integer projectId;

}
