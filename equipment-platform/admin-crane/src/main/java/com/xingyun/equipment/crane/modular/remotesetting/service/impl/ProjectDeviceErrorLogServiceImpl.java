package com.xingyun.equipment.crane.modular.remotesetting.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.remotesetting.dao.ProjectDeviceErrorLogMapper;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectDeviceErrorLog;
import com.xingyun.equipment.crane.modular.remotesetting.service.ProjectDeviceErrorLogService;
import org.apache.commons.lang.StringUtils;
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
public class ProjectDeviceErrorLogServiceImpl extends ServiceImpl<ProjectDeviceErrorLogMapper, ProjectDeviceErrorLog> implements ProjectDeviceErrorLogService {

    @Override
    public ResultDTO<DataDTO<List<ProjectDeviceErrorLog>>> getPageList(RequestDTO<ProjectDeviceErrorLog> request) {
        Page<ProjectDeviceErrorLog> page = new Page<>(request.getPageNum(), request.getPageSize());
        ProjectDeviceErrorLog projectDeviceErrorLog =request.getBody();
        EntityWrapper<ProjectDeviceErrorLog> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        if (StringUtils.isNotBlank(projectDeviceErrorLog.getDeviceNo())) {
            ew.eq("device_no", projectDeviceErrorLog.getDeviceNo());
        }
        if (StringUtils.isNotBlank(projectDeviceErrorLog.getType())) {
            ew.eq("type", projectDeviceErrorLog.getType());
        }
        if(projectDeviceErrorLog.getProjectId()!=null){
            ew.eq("project_id", projectDeviceErrorLog.getProjectId());
        }
        ew.orderBy("createTime",false);
        List<ProjectDeviceErrorLog> list = baseMapper.selectPage(page,ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }
}
