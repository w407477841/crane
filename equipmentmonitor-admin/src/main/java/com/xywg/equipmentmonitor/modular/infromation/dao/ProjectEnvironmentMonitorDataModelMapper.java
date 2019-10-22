package com.xywg.equipmentmonitor.modular.infromation.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectEnvironmentMonitorDataModel;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-08-24
 */
public interface ProjectEnvironmentMonitorDataModelMapper extends BaseMapper<ProjectEnvironmentMonitorDataModel> {
    /**
     * 根据名称查询
     * @param ew2
     * @return
     * @author yuanyang
     */
    ProjectEnvironmentMonitorDataModel selectByName( @Param("ew")EntityWrapper<ProjectEnvironmentMonitorDataModel> ew2);
}
