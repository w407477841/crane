package com.xingyun.equipment.crane.modular.device.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xingyun.equipment.core.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 安全帽
 * </p>
 *
 * @author hjy
 * @since 2018-11-23
 */
@TableName("t_project_helmet")
@Data
public class ProjectHelmet extends BaseEntity<ProjectHelmet> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * imei
     */
	private String imei;
    /**
     * 设备名称
     */
	private String name;
    /**
     * 在线状态:1在线,0离线
     */
	@TableField("is_online")
	private Integer isOnline;
    /**
     * 启用状态:1启用,0未启用
     */
	private Integer status;
    /**
     * 备注
     */
	private String comments;


	/**
	 * 是否是最新绑定数据(0:当前绑定,1:代表是绑定履历数据)
 	 */
	@TableField("current_flag")
	private Integer currentFlag;
    /**
     * 工程id
     */
	@TableField("project_id")
	private Integer projectId;

	@TableField(exist = false)
	private String projectName;

    /**
     * 证件类型,默认1:身份证
     */
	@TableField("id_card_type")
	private Integer idCardType;
    /**
     * 证件编号
     */
	@TableField("id_card_number")
	private String idCardNumber;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
