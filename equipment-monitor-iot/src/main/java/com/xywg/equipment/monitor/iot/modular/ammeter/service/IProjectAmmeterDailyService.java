package com.xywg.equipment.monitor.iot.modular.ammeter.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectAmmeterDaily;

/**
 * <p>
 * 电表每日统计 服务类
 * </p>
 *
 * @author hy
 * @since 2018-11-20
 */
public interface IProjectAmmeterDailyService extends IService<ProjectAmmeterDaily> {
    /**
     *  每日统计
     */
    public void daily();

}
