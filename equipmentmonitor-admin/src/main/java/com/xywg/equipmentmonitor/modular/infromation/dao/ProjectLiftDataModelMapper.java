package com.xywg.equipmentmonitor.modular.infromation.dao;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectLiftDataModel;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-08-24
 */
public interface ProjectLiftDataModelMapper extends BaseMapper<ProjectLiftDataModel> {
    /**
     * 根据设备类型和厂商查询
     * @param ew
     * @return
     * @author yuanyang
     */
    ProjectLiftDataModel selectByName(@Param("ew")EntityWrapper<ProjectLiftDataModel> ew);
}
