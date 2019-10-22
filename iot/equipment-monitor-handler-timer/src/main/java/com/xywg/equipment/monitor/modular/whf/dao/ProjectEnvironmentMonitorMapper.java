package com.xywg.equipment.monitor.modular.whf.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.whf.dto.DeviceStatusVO;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitor;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-21
 */
public interface ProjectEnvironmentMonitorMapper extends BaseMapper<ProjectEnvironmentMonitor> {
    /**
     *  查询设备状态
     * @return
     */
    public List<DeviceStatusVO> deviceStatus();

}
