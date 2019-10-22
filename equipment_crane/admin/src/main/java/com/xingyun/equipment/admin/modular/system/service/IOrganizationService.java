package com.xingyun.equipment.admin.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.projectmanagement.model.ProjectInfo;
import com.xingyun.equipment.admin.modular.system.model.Organization;
import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.admin.modular.system.model.User;
import com.xingyun.equipment.admin.modular.system.vo.OrganizationVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-16
 */
public interface IOrganizationService extends IService<Organization> {
    /**
     * 获取部门信息
     * @param requestDTO
     * @return
     */
    List<OrganizationVo> selectOrganizationInfo(RequestDTO<Organization> requestDTO);

    /**
     * 根据用户ID 获取组织
     * @param id
     * @return
     */
    List<Organization> getByUserId(Integer id);


    /**
     * 获取所有部门
     */
    List<Integer>  getOrgsByParent(Integer parentId);

    /**
     * 获取当前用户下的项目部
     * @param ids
     * @return
     * @throws Exception
     */
    List<Organization> selectOrganization(List<Integer> ids) throws Exception;

    /**
     * 获取当前用户下的项目部
     * @return
     * @throws Exception
     */
    List<Organization> selectUnderOrganization() throws Exception;
}
