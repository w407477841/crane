package com.xywg.equipment.monitor.modular.whf.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectCraneDetailMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectCraneDetail;
import com.xywg.equipment.monitor.modular.whf.service.IProjectCraneDetailService;
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
public class ProjectCraneDetailServiceImpl extends ServiceImpl<ProjectCraneDetailMapper, ProjectCraneDetail> implements IProjectCraneDetailService {

    @Override
    public void createDetail(ProjectCraneDetail projectCraneDetail, String tableName) {
        baseMapper.createDetail(projectCraneDetail,tableName);
    }
}
