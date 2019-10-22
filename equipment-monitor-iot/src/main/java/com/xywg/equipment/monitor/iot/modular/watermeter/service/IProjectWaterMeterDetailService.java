package com.xywg.equipment.monitor.iot.modular.watermeter.service;

import com.xywg.equipment.monitor.iot.modular.watermeter.model.ProjectWaterMeterDetail;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-09-17
 */
public interface IProjectWaterMeterDetailService extends IService<ProjectWaterMeterDetail> {
    /**
     * 根据电力id获取最近一次的明细
     * @author SJ
     * @param eleId
     * @return
     */
    ProjectWaterMeterDetail getLastInfo(int eleId);
}
