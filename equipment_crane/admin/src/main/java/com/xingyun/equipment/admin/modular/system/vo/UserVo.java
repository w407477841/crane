package com.xingyun.equipment.admin.modular.system.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xingyun.equipment.admin.modular.system.model.User;
import com.xingyun.equipment.admin.modular.system.model.UserRole;
import lombok.Data;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/9/10
 *TIME:11:02
 */
@Data
public class UserVo extends User {
    /**
     * 用户角色集合
     */
    @TableField(exist = false)
    private List<UserRole> userRoleList;

    /**
     * 用户所有角色
     */
    @TableField(exist = false)
    private List<String> userRole;

    /**
     * 原密码
     */
    @TableField(exist = false)
    private String oldPassword;

    /**
     * 用户所有角色id
     */
    @TableField(exist = false)
    private List<Integer> roleIds;


}
