package com.xywg.equipmentmonitor.modular.remotesetting.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.remotesetting.dao.ProjectCraneCalibrationLogMapper;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectCraneCalibrationLog;
import com.xywg.equipmentmonitor.modular.remotesetting.service.ProjectCraneCalibrationLogService;
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
public class ProjectCraneCalibrationLogServiceImpl extends ServiceImpl<ProjectCraneCalibrationLogMapper, ProjectCraneCalibrationLog> implements ProjectCraneCalibrationLogService {

    @Override
    public ResultDTO<DataDTO<List<ProjectCraneCalibrationLog>>> getPageList(RequestDTO<ProjectCraneCalibrationLog> request) {
        Page<ProjectCraneCalibrationLog> page = new Page<>(request.getPageNum(), request.getPageSize());
        ProjectCraneCalibrationLog projectDeviceErrorLog =request.getBody();
        EntityWrapper<ProjectCraneCalibrationLog> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        if (StringUtils.isNotBlank(projectDeviceErrorLog.getDeviceNo())) {
            ew.eq("device_no", projectDeviceErrorLog.getDeviceNo());
        }
        if (StringUtils.isNotBlank(projectDeviceErrorLog.getCommand())) {
            ew.eq("command", projectDeviceErrorLog.getCommand());
        }
        if(projectDeviceErrorLog.getProjectId()!=null){
            ew.eq("project_id", projectDeviceErrorLog.getProjectId());
        }
        ew.orderBy("createTime",false);
        List<ProjectCraneCalibrationLog> list = baseMapper.selectPage(page,ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }
}
