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
@TableName("PER_INF")
public class PerInf extends Model<PerInf> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增长
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 人员编号，内部使用
     */
	@TableField("Per_Code")
	private String PerCode;
    /**
     * 人员姓名
     */
	@TableField("Per_Name")
	private String PerName;
	@TableField("Id_No")
	private String IdNo;
    /**
     * 所属项目编码
     */
	@TableField("Project_Code")
	private String ProjectCode;
    /**
     * 用于存放身份证照片
     */
	@TableField("Id_Image")
	private byte[] IdImage;
    /**
     * 用于存放其他照片
     */
	private byte[] Phote;


	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
