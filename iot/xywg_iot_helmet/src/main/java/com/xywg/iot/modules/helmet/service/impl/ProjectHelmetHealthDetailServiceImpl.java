package com.xywg.iot.modules.helmet.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.modules.helmet.dao.ProjectHelmetHealthDetailMapper;
import com.xywg.iot.modules.helmet.model.ProjectHelmetHealthDetail;
import com.xywg.iot.modules.helmet.service.ProjectHelmetHealthDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 */
@Service
public class ProjectHelmetHealthDetailServiceImpl extends ServiceImpl<ProjectHelmetHealthDetailMapper, ProjectHelmetHealthDetail> implements ProjectHelmetHealthDetailService {

    @Autowired
    ProjectHelmetHealthDetailMapper projectHelmetHealthDetailMapper;


    @Override
    public void insertByMonth(ProjectHelmetHealthDetail projectHelmetHealthDetail) {
        projectHelmetHealthDetailMapper.insertByMonth(projectHelmetHealthDetail);
    }
}
