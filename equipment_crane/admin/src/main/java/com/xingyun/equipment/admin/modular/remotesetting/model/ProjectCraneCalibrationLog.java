package com.xingyun.equipment.admin.modular.remotesetting.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 塔吊校准设备履历
 * @author hjy
 */
@Data
@TableName("t_project_crane_calibration_log")
public class ProjectCraneCalibrationLog extends Model<ProjectCraneCalibrationLog> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备编号
     */
    @TableField("device_no")
    private String deviceNo;

    /**
     * 工程名称
     */
    @TableField("project_id")
    private Integer projectId;
    /**
     * 所属命令
     */
    @TableField("command")
    private String command;

    /**
     * 所属命令
     */
    @TableField("content")
    private String content;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    /**
     * 删除标志
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDel;
    /**
     * 创建日期
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 创建人
     */
    @TableField(value="create_user",fill = FieldFill.INSERT)
    private Integer createUser;
    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private String createUserName;
    /**
     * 修改日期
     */
    @TableField(value = "modify_time", fill = FieldFill.UPDATE)
    private Date modifyTime;
    /**
     * 修改人
     */
    @TableField(value="modify_user", fill = FieldFill.UPDATE)
    private Integer modifyUser;


    public ProjectCraneCalibrationLog(String deviceNo, Integer projectId, String command, String content, Integer isDel, Date createTime) {
        this.deviceNo = deviceNo;
        this.projectId = projectId;
        this.command = command;
        this.content = content;
        this.isDel = isDel;
        this.createTime =createTime;
    }

    public ProjectCraneCalibrationLog() {
    }
}
