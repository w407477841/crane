package com.xywg.attendance.modular.business.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
@TableName("AM_INF")
public class AmInf extends Model<AmInf> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键、自增长
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long id;
    /**
     * 考勤机设备号
     */
	@TableField("AM_Code")
	private String aMCode;
    /**
     * 所属项目编号
     */
	@TableField("Project_Code")
	private String projectCode;
    /**
     * 考勤机类型(这里存的是编号，取的国家实名制平台考勤机类型字典，请参考)
     */
	@TableField("AM_Type")
	private String aMType;

	/**
	 * 设备绑定的考勤方向(1为进场，0为出场)
	 */
	@TableField("At_Type")
	private Integer atType;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
