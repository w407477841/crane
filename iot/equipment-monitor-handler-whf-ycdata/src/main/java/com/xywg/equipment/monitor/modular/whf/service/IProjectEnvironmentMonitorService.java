package com.xywg.equipment.monitor.modular.whf.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitor;

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
