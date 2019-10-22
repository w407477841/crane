package com.xywg.equipment.monitor.modular.whf.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectEnvironmentMonitorAlarmMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitorAlarm;
import com.xywg.equipment.monitor.modular.whf.service.IProjectEnvironmentMonitorAlarmService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-21
 */
@Service
public class ProjectEnvironmentMonitorAlarmServiceImpl extends ServiceImpl<ProjectEnvironmentMonitorAlarmMapper, ProjectEnvironmentMonitorAlarm> implements IProjectEnvironmentMonitorAlarmService {

    @Override
    public void createAlarm(ProjectEnvironmentMonitorAlarm alarm, String tableName) throws Exception {

        baseMapper.createAlarm(alarm,tableName);

    }
}
