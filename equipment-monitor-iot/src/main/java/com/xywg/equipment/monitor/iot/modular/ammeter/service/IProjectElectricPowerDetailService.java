package com.xywg.equipment.monitor.iot.modular.ammeter.service;

import com.baomidou.mybatisplus.activerecord.Model;
import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPowerDetail;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
public interface IProjectElectricPowerDetailService extends IService<ProjectElectricPowerDetail> {

    /**
     * 根据电力id获取最近一次的明细
     * @author SJ
     * @param eleId
     * @return
     */
    ProjectElectricPowerDetail getLastInfo(int eleId);

    String getYearOnYear();

    String getRingRatio();

}
