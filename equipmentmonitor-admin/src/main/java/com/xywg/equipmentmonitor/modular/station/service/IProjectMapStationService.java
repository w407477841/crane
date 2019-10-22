package com.xywg.equipmentmonitor.modular.station.service;

import com.xywg.equipmentmonitor.modular.station.model.ProjectMapStation;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2019-03-20
 */
public interface IProjectMapStationService extends IService<ProjectMapStation> {
    void deleteByMapId(Integer mapId);
}
