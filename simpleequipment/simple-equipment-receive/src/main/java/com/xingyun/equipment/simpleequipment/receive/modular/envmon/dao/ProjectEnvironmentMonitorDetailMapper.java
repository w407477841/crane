package com.xingyun.equipment.simpleequipment.receive.modular.envmon.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xingyun.equipment.simpleequipment.receive.modular.envmon.model.ProjectEnvironmentMonitorDetail;
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
