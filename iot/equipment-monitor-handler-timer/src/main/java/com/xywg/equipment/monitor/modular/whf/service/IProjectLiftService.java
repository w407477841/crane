package com.xywg.equipment.monitor.modular.whf.service;


import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLift;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-22
 */
public interface IProjectLiftService extends IService<ProjectLift> {
    /**
     * 查询 单条
     * @param deviceNo
     * @return
     */
    ProjectLift  selectOne(String deviceNo);

}
