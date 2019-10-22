package com.xywg.equipment.monitor.modular.yc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.yc.dao.ProjectEnvironmentMonitorMapper;
import com.xywg.equipment.monitor.modular.yc.model.ProjectEnvironmentMonitor;
import com.xywg.equipment.monitor.modular.yc.service.IProjectEnvironmentMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectEnvironmentMonitorServiceImpl extends ServiceImpl<ProjectEnvironmentMonitorMapper, ProjectEnvironmentMonitor> implements IProjectEnvironmentMonitorService {

    @Autowired
    private ProjectEnvironmentMonitorMapper mapper;

    /**
     * 获取需要转发的扬尘设备号
     */
    @Override
    public List<String> selectDeviceNoOfNeedDispatch() {
        List<String> result= mapper.selectDeviceNoOfNeedDispatch(1);
        return  result;
    }
}
