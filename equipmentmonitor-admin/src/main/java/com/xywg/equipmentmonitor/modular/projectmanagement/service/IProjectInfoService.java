package com.xywg.equipmentmonitor.modular.projectmanagement.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectInfoVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
public interface IProjectInfoService extends IService<ProjectInfo> {
    /**
     * 新增
     * @param projectInfo
     * @return
     */
    @Override
    boolean insert(ProjectInfo projectInfo);

    /**
     * 修改
     * @param projectInfo
     * @return
     */
    @Override
    boolean updateById(ProjectInfo projectInfo);

    /**
     * 获取项目信息
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<ProjectInfoVo> getProjectInfo(RequestDTO<ProjectInfoVo> requestDTO) throws Exception;

    /**
     * 获取项目列表
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<ProjectInfo> selectProject(RequestDTO<ProjectInfo> requestDTO) throws Exception;

    /**
     * 绑定智慧工地
     * @param requestDTO
     * @return
     * @throws Exception
     */
    boolean bindingProject(RequestDTO<ProjectInfoVo> requestDTO) throws Exception;

    /**
     * 获取智慧工地项目列表
     * @param requestDTO
     * @return
     * @throws Exception
     */
    String getSmartProject(RequestDTO<ProjectInfo> requestDTO) throws Exception;

    /**
     * 获取项目信息
     * @param page
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<ProjectInfoVo> selectProjectInfo(Page<ProjectInfoVo> page,RequestDTO<ProjectInfoVo> requestDTO) throws Exception;

    /**
     * 根据id获取项目信息
     * @param requestDTO
     * @return
     * @throws Exception
     */
    ProjectInfoVo selectProjectById(RequestDTO<ProjectInfoVo> requestDTO) throws Exception;

    /**
     * 获取超标工地数量
     * @param orgId
     * @return
     * @throws Exception
     */
    Integer selectExceedCountByOrgId(Integer orgId) throws Exception;

    /**
     * 获取工地数量
     * @return
     * @throws Exception
     */
    Integer getProjectCount() throws Exception;

    /**
     * 智慧工地新增项目
     * @param requestDTO
     * @return
     * @throws Exception
     */
    boolean insertProject(ProjectInfo requestDTO) throws Exception;
}
