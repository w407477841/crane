package com.xywg.equipment.monitor.modular.whf.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author hy
 * @since 2019-07-11
 */
@Data
@TableName("t_project_unload_alarm")
public class ProjectUnloadAlarm extends Model<ProjectUnloadAlarm> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 卸料id
     */
    @TableField("unload_id")
    private Integer unloadId;
    /**
     * 实时数据ID
     */
    @TableField("detail_id")
    private Integer detailId;
    /**
     * 报警id
     */
    @TableField("alarm_id")
    private Integer alarmId;
    /**
     * 设备编号
     */
    @TableField("device_no")
    private String deviceNo;
    /**
     * 报警内容
     */
    @TableField("alarm_info")
    private String alarmInfo;
    /**
     * 删除标志
     */
    @TableField("is_del")
    private Integer isDel;
    /**
     * 创建日期
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 结束日期
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 创建人
     */
    @TableField("create_user")
    private Integer createUser;
    /**
     * 修改日期
     */
    @TableField("modify_time")
    private Date modifyTime;
    /**
     * 修改人（处理人id）
     */
    @TableField("modify_user")
    private Integer modifyUser;
    /**
     * 处理人姓名
     */
    @TableField("modify_user_name")
    private String modifyUserName;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 报警级别
     */
    @TableField("alarm_class")
    private Integer alarmClass;
    /**
     * 报警日期
     */
    @TableField("alarm_time")
    private Date alarmTime;

    /**
     * 是否处理
     */
    @TableField("is_handle")
    private int isHandle;
    /**
     * 处理意见
     */
    @TableField("comments")
    private int comments;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ProjectUnloadAlarm{" +
                "id=" + id +
                ", unloadId=" + unloadId +
                ", detailId=" + detailId +
                ", alarmId=" + alarmId +
                ", deviceNo=" + deviceNo +
                ", alarmInfo=" + alarmInfo +
                ", isDel=" + isDel +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", modifyTime=" + modifyTime +
                ", modifyUser=" + modifyUser +
                ", modifyUserName=" + modifyUserName +
                ", status=" + status +
                "}";
    }
}
