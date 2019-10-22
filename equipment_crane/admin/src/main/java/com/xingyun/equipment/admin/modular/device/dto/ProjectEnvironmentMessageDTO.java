package com.xingyun.equipment.admin.modular.device.dto;

import com.xingyun.equipment.admin.core.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;


/**
 * @author hjy
 */
@Data
public class ProjectEnvironmentMessageDTO extends BaseEntity<ProjectEnvironmentMessageDTO> {
    private Integer id;
    /**模板**/
    private String model;
    /**标题**/
    private String title;
    /**内容**/
    private String content;
    /**发送人集合**/
    private String[] relatedUserArray;
    /**发送人**/
    private String relatedUser;
    /**发送时间**/
    private String sendTime;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
