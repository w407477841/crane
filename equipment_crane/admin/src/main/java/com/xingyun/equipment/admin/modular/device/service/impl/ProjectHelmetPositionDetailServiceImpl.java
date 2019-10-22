package com.xingyun.equipment.admin.modular.device.service.impl;

import com.xingyun.equipment.admin.modular.device.model.ProjectHelmetPositionDetail;
import com.xingyun.equipment.admin.modular.device.dao.ProjectHelmetPositionDetailMapper;
import com.xingyun.equipment.admin.modular.device.service.IProjectHelmetPositionDetailService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 安全帽定位明细(采集数据) 服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
@Service
public class ProjectHelmetPositionDetailServiceImpl extends ServiceImpl<ProjectHelmetPositionDetailMapper, ProjectHelmetPositionDetail> implements IProjectHelmetPositionDetailService {

    @Override
    public List<ProjectHelmetPositionDetail> getLastLocation(String tableName, String time, Integer projectId) {
        return baseMapper.getLastLocation(tableName,time,projectId);
    }

    @Override
    public List<ProjectHelmetPositionDetail> getLocations(List<String> tables, Integer projectId, String identityCode,  String beginTime, String endTime) {
        return baseMapper.getLocations(tables,  projectId,identityCode, beginTime,  endTime);
    }


}
