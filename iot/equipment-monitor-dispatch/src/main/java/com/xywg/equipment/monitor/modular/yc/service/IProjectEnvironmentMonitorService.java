package com.xywg.equipment.monitor.modular.yc.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.yc.model.ProjectEnvironmentMonitor;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qiyan
 * @since 2019-08-01
 */
public interface IProjectEnvironmentMonitorService extends IService<ProjectEnvironmentMonitor> {

    /**
     * 获取需要转发的扬尘设备号
     */
    List<String> selectDeviceNoOfNeedDispatch();
}
