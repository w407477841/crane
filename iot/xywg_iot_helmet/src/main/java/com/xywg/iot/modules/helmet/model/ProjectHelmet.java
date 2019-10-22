package com.xywg.iot.modules.helmet.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.iot.common.domain.BaseEntity;
import lombok.Data;

/**
 * @author hjy
 * @date 2018/11/22
 */
@Data
@TableName("t_project_helmet")
public class ProjectHelmet extends BaseEntity<ProjectHelmet> {

    private String imei;

    private String name;

    @TableField(value = "is_online")
    private Integer isOnline;

    private Integer status;

    /* @TableField(value = "org_id")
     private Integer orgId;*/

    @TableField(value = "current_flag")
    private Integer currentFlag;

    @TableField(value = "project_id")
    private Integer projectId;

    @TableField(exist = false)
    private String projectName;

    @TableField(value = "id_card_type")
    private Integer idCardType;

    @TableField(value = "id_card_number")
    private String idCardNumber;


}
