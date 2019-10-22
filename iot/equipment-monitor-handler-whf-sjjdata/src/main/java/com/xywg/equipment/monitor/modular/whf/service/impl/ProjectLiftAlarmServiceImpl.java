package com.xywg.equipment.monitor.modular.whf.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectLiftAlarmMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftAlarm;
import com.xywg.equipment.monitor.modular.whf.service.IProjectLiftAlarmService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-22
 */
@Service
public class ProjectLiftAlarmServiceImpl extends ServiceImpl<ProjectLiftAlarmMapper, ProjectLiftAlarm> implements IProjectLiftAlarmService {

    @Override
    public void createAlarm(ProjectLiftAlarm alarm, String tableName) {
        baseMapper.createAlarm(alarm,tableName);
    }
}
