package com.xywg.attendance.modular.business.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("Attendance_Record")
public class AttendanceRecord extends Model<AttendanceRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增长
     */
    @TableId(value = "Id", type = IdType.AUTO)
    private Long id;
    /**
     * 考勤机设备号
     */
    @TableField("Per_Code")
    private String perCode;
    /**
     * 所属项目编号
     */
    @TableField("Project_Code")
    private String projectCode;
    /**
     * 考勤记录
     */
    @TableField("Record")
    private Date record;
    /**
     * 考勤方向(1为进场，0为出场)
     */
    @TableField("At_Type")
    private Integer atType;

    @TableField("ID_NO")
    private String idNO;

    @TableField("photo_url")
    private String photoUrl;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public AttendanceRecord(String perCode, String projectCode, Date record, Integer atType, String idNO) {
        this.perCode = perCode;
        this.projectCode = projectCode;
        this.record = record;
        this.atType = atType;
        this.idNO = idNO;
    }


    public AttendanceRecord(String perCode, String projectCode, Date record, Integer atType, String idNO, String photo) {
        this.perCode = perCode;
        this.projectCode = projectCode;
        this.record = record;
        this.atType = atType;
        this.idNO = idNO;
        this.photoUrl = photo;
    }

    public AttendanceRecord() {
    }
}
