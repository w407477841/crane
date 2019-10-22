package com.xywg.iot.modules.helmet.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.modules.helmet.dao.ProjectHelmetAlarmMapper;
import com.xywg.iot.modules.helmet.model.ProjectHelmetAlarm;
import com.xywg.iot.modules.helmet.model.ProjectHelmetHealthDetail;
import com.xywg.iot.modules.helmet.service.ProjectHelmetAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 * @author admin
 */
@Service
public class ProjectHelmetAlarmServiceImpl extends ServiceImpl<ProjectHelmetAlarmMapper, ProjectHelmetAlarm> implements ProjectHelmetAlarmService {
    @Autowired
    private ProjectHelmetAlarmMapper  projectHelmetAlarmMapper;


    @Override
    public void insertBatchByMonth(String tableName, List<ProjectHelmetAlarm> list) {
        projectHelmetAlarmMapper.insertBatchByMonth(tableName,list);
    }

}
