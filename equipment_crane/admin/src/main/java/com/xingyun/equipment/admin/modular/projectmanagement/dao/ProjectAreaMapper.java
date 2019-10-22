package com.xingyun.equipment.admin.modular.projectmanagement.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xingyun.equipment.admin.modular.projectmanagement.model.ProjectArea;
import com.xingyun.equipment.admin.modular.projectmanagement.vo.ProjectAreaVo;
import com.xingyun.equipment.admin.modular.projectmanagement.vo.ProjectAreaVoVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-08-23
 */
public interface ProjectAreaMapper extends BaseMapper<ProjectArea> {
    /**
     * 获取区域列表
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectAreaVo> selectArea(Map<String,Object> map) throws Exception;

    /**
     * 获取区域信息
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectAreaVoVo> selectAreaInfo(Map<String,Object> map) throws Exception;
}
