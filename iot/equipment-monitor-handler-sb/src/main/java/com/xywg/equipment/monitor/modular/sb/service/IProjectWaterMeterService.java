package com.xywg.equipment.monitor.modular.sb.service;


import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.sb.model.ProjectWaterMeter;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-09-17
 */
public interface IProjectWaterMeterService extends IService<ProjectWaterMeter> {
    /**
     * 根据设备号获取设备基本信息
     * @param deviceNo
     * @return
     */
    ProjectWaterMeter getBaseInfo(String deviceNo);
}
