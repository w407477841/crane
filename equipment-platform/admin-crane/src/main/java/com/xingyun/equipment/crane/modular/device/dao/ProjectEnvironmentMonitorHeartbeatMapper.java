package com.xingyun.equipment.crane.modular.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentHeartbeat;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjy
 * @since 2018-08-21
 */
public interface ProjectEnvironmentMonitorHeartbeatMapper extends BaseMapper<ProjectEnvironmentHeartbeat> {

    /**
     * 查询列表
     * @param rowBounds
     * @param heartbeat
     * @return
     */
    List<ProjectEnvironmentHeartbeat> selectPageList(RowBounds rowBounds, @Param("ew") ProjectEnvironmentHeartbeat heartbeat);


}
