package com.xywg.equipmentmonitor.modular.remotesetting.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.remotesetting.dao.ProjectDeviceErrorLogMapper;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectDeviceErrorLog;
import com.xywg.equipmentmonitor.modular.remotesetting.service.ProjectDeviceErrorLogService;
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
