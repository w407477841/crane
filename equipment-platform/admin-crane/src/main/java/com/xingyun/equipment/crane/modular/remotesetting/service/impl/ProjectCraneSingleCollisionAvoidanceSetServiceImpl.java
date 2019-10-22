package com.xingyun.equipment.crane.modular.remotesetting.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.remotesetting.dao.ProjectCraneCalibrationLogMapper;
import com.xingyun.equipment.crane.modular.remotesetting.dao.ProjectCraneSingleCollisionAvoidanceSetMapper;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectCraneCalibrationLog;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectCraneSingleCollisionAvoidanceSet;
import com.xingyun.equipment.crane.modular.remotesetting.service.ProjectCraneCalibrationLogService;
import com.xingyun.equipment.crane.modular.remotesetting.service.ProjectCraneSingleCollisionAvoidanceSetService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xss
 * @since 2018-09-30
 */
@Service
public class ProjectCraneSingleCollisionAvoidanceSetServiceImpl extends ServiceImpl<ProjectCraneSingleCollisionAvoidanceSetMapper, ProjectCraneSingleCollisionAvoidanceSet> implements ProjectCraneSingleCollisionAvoidanceSetService {

    @Override
    public ResultDTO<DataDTO<List<ProjectCraneSingleCollisionAvoidanceSet>>> getPageList(RequestDTO<ProjectCraneSingleCollisionAvoidanceSet> request) {
        Page<ProjectCraneSingleCollisionAvoidanceSet> page = new Page<>(request.getPageNum(), request.getPageSize());
        ProjectCraneSingleCollisionAvoidanceSet projectDeviceErrorLog =request.getBody();
        EntityWrapper<ProjectCraneSingleCollisionAvoidanceSet> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        if (StringUtils.isNotBlank(projectDeviceErrorLog.getDeviceNo())) {
            ew.eq("device_no", projectDeviceErrorLog.getDeviceNo());
        }
        if (projectDeviceErrorLog.getPageRange()!=null) {
            ew.eq("page_range", projectDeviceErrorLog.getPageRange());
        }
        if(projectDeviceErrorLog.getProjectId()!=null){
            ew.eq("project_id", projectDeviceErrorLog.getProjectId());
        }
        ew.orderBy("createTime",false);
        List<ProjectCraneSingleCollisionAvoidanceSet> list = baseMapper.selectPage(page,ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }
}
