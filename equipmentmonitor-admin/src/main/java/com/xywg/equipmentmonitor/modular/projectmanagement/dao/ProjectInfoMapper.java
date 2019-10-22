package com.xywg.equipmentmonitor.modular.projectmanagement.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Mapper
public interface ProjectInfoMapper extends BaseMapper<ProjectInfo> {
    /**
     * 获取项目信息
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectInfoVo> getProjectInfo(Map<String,Object> map) throws Exception;

    /**
     * 获取项目列表
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectInfo> selectProject(Map<String,Object> map) throws Exception;

    /**
     * 绑定智慧工地
     * @param projectInfoVo
     * @return
     * @throws Exception
     */
    Integer bindingProject(ProjectInfoVo projectInfoVo) throws Exception;

    /**
     * 获取项目信息
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectInfoVo> selectProjectInfo(Page<ProjectInfoVo> page, Map<String,Object> map) throws Exception;

    /**
     * 根据名称获取项目信息
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectInfo> getProjectByName(Map<String,Object> map);

    /**
     * 根据id获取项目信息
     * @param map
     * @return
     * @throws Exception
     */
    ProjectInfoVo selectProjectById(Map<String,Object> map) throws Exception;

    /**
     * 获取超标工地数量
     * @param map
     * @return
     * @throws Exception
     */
    Integer selectExceedCountByOrgId(Map<String,Object> map) throws Exception;

    /**
     * 根据项目id获取设备
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectInfoVo> selectDeviceByProjectId(Map<String,Object> map);

    /**
     * 获取工地数量
     * @param map
     * @return
     * @throws Exception
     */
    Integer getProjectCount(Map<String,Object> map) throws Exception;
}
