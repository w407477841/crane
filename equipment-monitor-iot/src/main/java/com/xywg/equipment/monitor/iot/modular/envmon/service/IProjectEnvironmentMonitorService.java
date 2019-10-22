package com.xywg.equipment.monitor.iot.modular.envmon.service;

import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentMonitor;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-21
 */
public interface IProjectEnvironmentMonitorService extends IService<ProjectEnvironmentMonitor> {
    /**
     *  查询单条
     * @param deviceNo
     * @return
     */
    ProjectEnvironmentMonitor selectOne(String deviceNo);

}
