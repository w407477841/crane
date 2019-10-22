package com.xywg.equipment.monitor.iot.modular.ammeter.service;

import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPowerHeartbeat;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
public interface IProjectElectricPowerHeartbeatService extends IService<ProjectElectricPowerHeartbeat> {
    /**
     * 获取最新一条心跳数据
     * @param eleId
     * @return
     */
    public ProjectElectricPowerHeartbeat getLastInfo(int eleId);
}
