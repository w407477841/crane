package com.xywg.equipment.monitor.modular.whf.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectLiftDetailMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftDetail;
import com.xywg.equipment.monitor.modular.whf.service.IProjectLiftDetailService;
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
public class ProjectLiftDetailServiceImpl extends ServiceImpl<ProjectLiftDetailMapper, ProjectLiftDetail> implements IProjectLiftDetailService {

    @Override
    public void createDetail(ProjectLiftDetail detail, String tableName) {
        baseMapper.createDetail(detail,tableName);
    }
}
