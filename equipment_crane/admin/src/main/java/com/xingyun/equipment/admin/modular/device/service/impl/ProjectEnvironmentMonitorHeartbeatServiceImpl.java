package com.xingyun.equipment.admin.modular.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.dao.ProjectEnvironmentMonitorHeartbeatMapper;
import com.xingyun.equipment.admin.modular.device.model.ProjectEnvironmentHeartbeat;
import com.xingyun.equipment.admin.modular.device.service.ProjectEnvironmentMonitorHeartbeatService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjy
 * @since 2018-08-21
 */
@Service
public class ProjectEnvironmentMonitorHeartbeatServiceImpl extends ServiceImpl<ProjectEnvironmentMonitorHeartbeatMapper, ProjectEnvironmentHeartbeat> implements ProjectEnvironmentMonitorHeartbeatService {


    @Override
    public ResultDTO<DataDTO<List<ProjectEnvironmentHeartbeat>>> selectPageList(RequestDTO<ProjectEnvironmentHeartbeat> res) {
        Page<ProjectEnvironmentHeartbeat> page = new Page<>(res.getPageNum(), res.getPageSize());
        List<ProjectEnvironmentHeartbeat> list = baseMapper.selectPageList(page,res.getBody());
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }
}
