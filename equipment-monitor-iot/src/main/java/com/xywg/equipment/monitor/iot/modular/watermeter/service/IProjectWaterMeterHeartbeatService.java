package com.xywg.equipment.monitor.iot.modular.watermeter.service;

import com.xywg.equipment.monitor.iot.modular.watermeter.model.ProjectWaterMeterHeartbeat;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-09-17
 */
public interface IProjectWaterMeterHeartbeatService extends IService<ProjectWaterMeterHeartbeat> {
    /**
     * 获取最新一条心跳数据
     * @param eleId
     * @return
     */
    public ProjectWaterMeterHeartbeat getLastInfo(int eleId);
}
