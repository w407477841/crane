package com.xywg.equipment.monitor.iot.modular.watermeter.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.iot.modular.watermeter.dao.ProjectWaterMeterHeartbeatMapper;
import com.xywg.equipment.monitor.iot.modular.watermeter.model.ProjectWaterMeterHeartbeat;
import com.xywg.equipment.monitor.iot.modular.watermeter.service.IProjectWaterMeterHeartbeatService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-09-17
 */
@Service
public class ProjectWaterMeterHeartbeatServiceImpl extends ServiceImpl<ProjectWaterMeterHeartbeatMapper, ProjectWaterMeterHeartbeat> implements IProjectWaterMeterHeartbeatService {
    @Override
    public ProjectWaterMeterHeartbeat getLastInfo(int eleId) {

        return baseMapper.getLastInfo(eleId);
    }
}
