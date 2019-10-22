package com.xingyun.equipment.system.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xingyun.equipment.system.model.Organization;
import com.xingyun.equipment.system.model.User;
import com.xingyun.equipment.system.model.UserRole;
import lombok.Data;
import org.springframework.beans.BeanUtils;

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
    @TableField(exist = false)
    private List<Organization> groups;

    public static UserVo factory(User user){

        UserVo  userVO = new UserVo();

        BeanUtils.copyProperties(user, userVO);

        return userVO;
    }

}
