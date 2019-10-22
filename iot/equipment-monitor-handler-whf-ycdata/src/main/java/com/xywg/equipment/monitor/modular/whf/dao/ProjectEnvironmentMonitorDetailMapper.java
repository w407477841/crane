package com.xywg.equipment.monitor.modular.whf.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitorDetail;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-21
 */
public interface ProjectEnvironmentMonitorDetailMapper extends BaseMapper<ProjectEnvironmentMonitorDetail> {
    /**
     *  添加明细数据
     * @param detail
     * @param tableName
     */
    void createDetail(@Param("t") ProjectEnvironmentMonitorDetail detail, @Param("tableName") String tableName);

}
