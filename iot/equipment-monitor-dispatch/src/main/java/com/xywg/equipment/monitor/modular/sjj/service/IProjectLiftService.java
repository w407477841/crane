package com.xywg.equipment.monitor.modular.sjj.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.sjj.model.ProjectLift;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qiyan
 * @since 2019-08-01
 */
public interface IProjectLiftService extends IService<ProjectLift> {

    /**
     * 获取需要转发的升降机设备号
     */
    List<String> selectDeviceNoOfNeedDispatch();
}
