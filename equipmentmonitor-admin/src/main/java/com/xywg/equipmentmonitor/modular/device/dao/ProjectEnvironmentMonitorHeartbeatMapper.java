package com.xywg.equipmentmonitor.modular.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentHeartbeat;
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
