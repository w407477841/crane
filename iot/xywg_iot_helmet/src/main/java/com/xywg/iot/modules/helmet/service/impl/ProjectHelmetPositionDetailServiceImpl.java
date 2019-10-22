package com.xywg.iot.modules.helmet.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.modules.helmet.dao.ProjectHelmetPositionDetailMapper;
import com.xywg.iot.modules.helmet.model.ProjectHelmetPositionDetail;
import com.xywg.iot.modules.helmet.service.ProjectHelmetPositionDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 */
@Service
public class ProjectHelmetPositionDetailServiceImpl extends ServiceImpl<ProjectHelmetPositionDetailMapper, ProjectHelmetPositionDetail> implements ProjectHelmetPositionDetailService {
    @Autowired
    private ProjectHelmetPositionDetailMapper projectHelmetPositionDetailMapper;

    @Override
    public void insertByMonth(String tableName, ProjectHelmetPositionDetail projectHelmetPositionDetail) {
        projectHelmetPositionDetailMapper.insertByMonth(tableName,projectHelmetPositionDetail);
    }
}
