package com.xywg.iot.modular.station.service;

import com.xywg.iot.modular.station.model.ProjectMapStation;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2019-03-21
 */
public interface IProjectMapStationService extends IService<ProjectMapStation> {
    ProjectMapStation selectByDeviceId(Integer id,Integer stationNo);
    ProjectMapStation selectByDeviceId(Integer id);
}
