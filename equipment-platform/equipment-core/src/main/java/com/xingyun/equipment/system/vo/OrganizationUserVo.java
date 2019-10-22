package com.xingyun.equipment.system.vo;

import com.xingyun.equipment.system.model.OrganizationUser;
import lombok.Data;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/9/11
 *TIME:18:04
 */
@Data
public class OrganizationUserVo extends OrganizationUser {
    private List<OrganizationUser> orgUsers;
}
