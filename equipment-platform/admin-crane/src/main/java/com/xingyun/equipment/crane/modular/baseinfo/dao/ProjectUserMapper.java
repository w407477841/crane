package com.xingyun.equipment.crane.modular.baseinfo.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.crane.modular.baseinfo.model.ProjectUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xingyun.equipment.crane.modular.baseinfo.vo.ProjectUserVo;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentMonitorAlarm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

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
public interface ProjectUserMapper extends BaseMapper<ProjectUser> {
    /**
     * 获取人员列表
     * @param map
     * @return
     */
    List<ProjectUser> selectUser(Map<String, Object> map);

    /**
     * 获取人员列表by ProjectId
     * @return
     */
    List<ProjectUser> selectUserByProjectId(@Param("projectId") Integer projectId);

    List<ProjectUser> selectPageList(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO<ProjectUser>> wrapper);

    /**
     * 获取人员信息列表
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectUserVo> selectUserInfo(Page<ProjectUserVo> page, Map<String, Object> map) throws Exception;

    /**
     * 根据身份证号获取人员信息
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectUser> selectProjectByCode(Map<String, Object> map);
}
