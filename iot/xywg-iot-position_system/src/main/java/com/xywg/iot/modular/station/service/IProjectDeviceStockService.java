package com.xywg.iot.modular.station.service;

import com.xywg.iot.modular.station.model.ProjectDevice;
import com.xywg.iot.modular.station.model.ProjectDeviceStock;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2019-03-21
 */
public interface IProjectDeviceStockService extends IService<ProjectDeviceStock> {
    /**
     *  查询
     * @param deviceNo
     * @return
     */
    ProjectDevice selectStationByDeviceNo(String deviceNo);
    /**
     *
     * 标签
     * * */
    ProjectDevice   selectTagByDeviceNo(String deviceNo);
}
