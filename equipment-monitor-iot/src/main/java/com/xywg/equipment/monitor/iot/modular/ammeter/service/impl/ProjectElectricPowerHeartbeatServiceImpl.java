package com.xywg.equipment.monitor.iot.modular.ammeter.service.impl;

import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPowerDetail;
import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPowerHeartbeat;
import com.xywg.equipment.monitor.iot.modular.ammeter.dao.ProjectElectricPowerHeartbeatMapper;
import com.xywg.equipment.monitor.iot.modular.ammeter.service.IProjectElectricPowerHeartbeatService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
@Service
public class ProjectElectricPowerHeartbeatServiceImpl extends ServiceImpl<ProjectElectricPowerHeartbeatMapper, ProjectElectricPowerHeartbeat> implements IProjectElectricPowerHeartbeatService {
    @Override
    public ProjectElectricPowerHeartbeat getLastInfo(int eleId) {

        return baseMapper.getLastInfo(eleId);
    }
}
