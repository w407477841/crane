package com.xywg.equipment.monitor.modular.whf.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectEnvironmentMonitorDetailMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitorDetail;
import com.xywg.equipment.monitor.modular.whf.service.IProjectEnvironmentMonitorDetailService;
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
public class ProjectEnvironmentMonitorDetailServiceImpl extends ServiceImpl<ProjectEnvironmentMonitorDetailMapper, ProjectEnvironmentMonitorDetail> implements IProjectEnvironmentMonitorDetailService {

    @Override
    public void createDetail(ProjectEnvironmentMonitorDetail detail, String tableName) throws Exception {
                baseMapper.createDetail(detail,tableName);
    }
}
