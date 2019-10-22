package com.xywg.iot.modules.helmet.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hjy
 * @date 2018/11/22
 */
@Data
@TableName("t_project_helmet_heartbeat")
public class ProjectHelmetHeartbeat extends Model<ProjectHelmetHeartbeat> {

    private Integer id;

    @TableField(value = "helmet_id")
    private Integer helmetId;

    private String imei;

    private Integer status;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "end_time")
    private Date endTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
