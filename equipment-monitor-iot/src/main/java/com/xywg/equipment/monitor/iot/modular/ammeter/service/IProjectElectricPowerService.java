package com.xywg.equipment.monitor.iot.modular.ammeter.service;

import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPower;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
public interface IProjectElectricPowerService extends IService<ProjectElectricPower> {

    /**
     * 根据设备号获取设备基本信息
     * @param deviceNo
     * @return
     */
    ProjectElectricPower getBaseInfo(String deviceNo);

}
