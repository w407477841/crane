package com.xywg.equipment.monitor.modular.whf.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectCraneAlarmMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectCraneAlarm;
import com.xywg.equipment.monitor.modular.whf.service.IProjectCraneAlarmService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Service
public class ProjectCraneAlarmServiceImpl extends ServiceImpl<ProjectCraneAlarmMapper, ProjectCraneAlarm> implements IProjectCraneAlarmService {

    @Override
    public void createAlarm(ProjectCraneAlarm alarm, String tableName) {
        baseMapper.createAlarm(alarm,tableName);
    }
}
