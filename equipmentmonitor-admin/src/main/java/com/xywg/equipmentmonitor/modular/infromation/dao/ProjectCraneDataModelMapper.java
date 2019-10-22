package com.xywg.equipmentmonitor.modular.infromation.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectCraneDataModel;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-08-24
 */
public interface ProjectCraneDataModelMapper extends BaseMapper<ProjectCraneDataModel> {
    /**
     * 根据条件查询单条
     * @param ew2
     * @return
     * @author yuanyang
     */
    ProjectCraneDataModel selectByName( @Param("ew")EntityWrapper<ProjectCraneDataModel> ew2);
}
