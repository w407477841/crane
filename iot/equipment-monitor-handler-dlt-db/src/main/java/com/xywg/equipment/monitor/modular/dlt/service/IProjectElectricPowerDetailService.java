package com.xywg.equipment.monitor.modular.dlt.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.dlt.model.ProjectElectricPowerDetail;

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
