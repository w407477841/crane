package com.xywg.equipment.monitor.modular.tj.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.tj.model.ProjectCrane;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qiyan
 * @since 2019-08-06
 */
public interface IProjectCraneService extends IService<ProjectCrane> {

    /**
     * 获取需要转发的升降机设备号
     */
    List<String> selectDeviceNoOfNeedDispatch();
}
