package com.xywg.attendance.modular.command.model;

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
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 考勤机设备号
     */
	@TableField("Per_Code")
	private String PerCode;
    /**
     * 所属项目编号
     */
	@TableField("Project_Code")
	private String ProjectCode;
    /**
     * 考勤记录
     */
	private Date Record;
    /**
     * 考勤方向(1为进场，0为出场)
     */
	@TableField("At_Type")
	private String AtType;


	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
