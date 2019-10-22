package com.xingyun.equipment.system.model;

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
@TableName("t_sys_organization_user")
public class OrganizationUser extends Model<OrganizationUser> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 所属部门
     */
    @TableField(value = "org_id")
    private Integer orgId;
    /**
     * 所属集团
     */
    @TableField("group_id")
    private Integer groupId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String comments;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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
        return "OrganizationUser{" +
        "id=" + id +
        ", userId=" + userId +
        ", orgId=" + orgId +
        ", groupId=" + groupId +
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
