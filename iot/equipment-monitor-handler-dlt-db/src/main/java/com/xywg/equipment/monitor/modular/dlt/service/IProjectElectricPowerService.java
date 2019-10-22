package com.xywg.equipment.monitor.modular.dlt.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.dlt.model.ProjectElectricPower;

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
