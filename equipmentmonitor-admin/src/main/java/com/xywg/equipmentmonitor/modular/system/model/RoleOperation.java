package com.xywg.equipmentmonitor.modular.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf123
 * @since 2018-08-13
 */
@TableName("t_sys_role_operation")
public class RoleOperation extends Model<RoleOperation> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 角色id
     */
    @TableField("role_id")
    private Integer roleId;
    /**
     * 所属部门
     */
    @TableField("oper_id")
    private Integer operId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String comments;
    /**
     * 删除标志
     */
    /**
     * 
     */
    @TableLogic
    @TableField(value="is_del",fill=FieldFill.INSERT)
    private Integer isDel;
    /**
     * 创建日期
     */
    @TableField(value="create_time",fill=FieldFill.INSERT)
    private Date createTime;
    /**
     * 创建人
     */
    @TableField(value="create_user",fill=FieldFill.INSERT)
    private Integer createUser;
    /**
     * 修改日期
     */
    @TableField(value="modify_time",fill=FieldFill.UPDATE)
    private Date modifyTime;
    /**
     * 修改人
     */
    @TableField(value="modify_user",fill=FieldFill.UPDATE)
    private Integer modifyUser;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getOperId() {
        return operId;
    }

    public void setOperId(Integer operId) {
        this.operId = operId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(Integer modifyUser) {
        this.modifyUser = modifyUser;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "RoleOperation{" +
        "id=" + id +
        ", roleId=" + roleId +
        ", operId=" + operId +
        ", status=" + status +
        ", comments=" + comments +
        ", isDel=" + isDel +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        ", modifyTime=" + modifyTime +
        ", modifyUser=" + modifyUser +
        "}";
    }
}
