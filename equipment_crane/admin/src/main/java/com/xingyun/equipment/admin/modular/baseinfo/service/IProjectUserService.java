package com.xingyun.equipment.admin.modular.baseinfo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.baseinfo.model.ProjectUser;
import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.admin.modular.baseinfo.vo.ProjectUserVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
public interface IProjectUserService extends IService<ProjectUser> {
    /**
     * 获取人员列表
     * @param requestDTO
     * @return
     */
    List<ProjectUser> selectUser(RequestDTO<ProjectUser> requestDTO);

    /**
     * 获取人员信息列表
     * @param page
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<ProjectUserVo> selectUserInfo(Page<ProjectUserVo> page, RequestDTO<ProjectUserVo> requestDTO) throws Exception;
}
