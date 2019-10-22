package com.xywg.equipmentmonitor.modular.infromation.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.xywg.equipmentmonitor.core.model.BaseEntity;
import com.xywg.equipmentmonitor.modular.system.model.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@TableName("t_project_message_model")
@Data
public class ProjectMessageModel extends BaseEntity<ProjectMessageModel> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 短信编号
     */
	private String code;
    /**
     * 短信标题
     */
	private String title;
    /**
     * 设备类型
     */
	@TableField("device_type")
	private Integer deviceType;
    /**
     * 预计开始发送时间
     */
	@TableField("send_time")
	private Date sendTime;
    /**
     * 短信类型
     */
	private Integer type;
    /**
     * 启用
     */
	private Integer status;
    /**
     * 内容
     */
	private String content;
    /**
     * 指定人
     */
	@TableField("related_user")
	private String relatedUser;
   
    /**
     * 组织结构
     */
	@TableField(value="org_id",fill = FieldFill.INSERT)
	private Integer orgId;


	@TableField(exist = false)
	private List<User> userList;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
