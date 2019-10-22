package com.xywg.equipment.monitor.iot.modular.spray.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSpray;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSprayBind;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSprayBindVo;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSprayDetail;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
public interface ProjectSprayBindService extends IService<ProjectSprayBind> {

    /**
     * 根据扬尘设备Id  查询当前绑定的喷淋
     * @param environmentId
     * @return
     */
    List<ProjectSpray> getListProjectSprayBind(Integer environmentId);
}
