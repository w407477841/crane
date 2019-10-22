package com.xywg.equipment.monitor.iot.modular.base.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
public interface IProjectInfoService extends IService<ProjectInfo> {
    /**
     *
     * @param id
     * @return
     */
    ProjectInfo selectCacheOne(Integer id);


}
