package com.xywg.equipmentmonitor.modular.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.xywg.equipmentmonitor.core.model.BaseEntity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf123
 * @since 2018-08-13
 */
@TableName("t_sys_user")
@Data
public class User extends BaseEntity<User> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 所属部门
     */
    @TableField(value = "org_id",fill = FieldFill.INSERT)
    private Integer orgId;
    /**
     * 用户编码
     */
    private String code;
    /**
     * 用户手机
     */
    private String phone;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 来源标记
     */
    private Integer flag;
    /**
     * 关联用户id
     */
    @TableField("relation_user_id")
    private Integer relationUserId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String comments;
    /**
     * 厂家
     */
    private String vender ;





    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "User{" +
        "id=" + id +
        ", orgId=" + orgId +
        ", code=" + code +
        ", phone=" + phone +
        ", name=" + name +
        ", password=" + password +
        ", flag=" + flag +
        ", relationUserId=" + relationUserId +
        ", status=" + status +
        ", comments=" + comments +
//        ", isDel=" + isDel +
//        ", createTime=" + createTime +
//        ", createUser=" + createUser +
//        ", modifyTime=" + modifyTime +
//        ", modifyUser=" + modifyUser +
        "}";
    }
}
