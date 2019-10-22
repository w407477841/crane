package com.xingyun.equipment.admin.modular.remotesetting.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.remotesetting.dao.ProjectDeviceRestartRecordMapper;
import com.xingyun.equipment.admin.modular.remotesetting.model.ProjectDeviceRestartRecord;
import com.xingyun.equipment.admin.modular.remotesetting.service.ProjectDeviceRestartRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjy
 * @since 2018-09-30
 */
@Service
public class ProjectDeviceRestartRecordServiceImpl extends ServiceImpl<ProjectDeviceRestartRecordMapper, ProjectDeviceRestartRecord> implements ProjectDeviceRestartRecordService {

    @Override
    public ResultDTO<DataDTO<List<ProjectDeviceRestartRecord>>> getPageList(RequestDTO<ProjectDeviceRestartRecord> request) {
        Page<ProjectDeviceRestartRecord> page = new Page<>(request.getPageNum(), request.getPageSize());
        ProjectDeviceRestartRecord restartRecord = request.getBody();
        EntityWrapper<RequestDTO> ew = new EntityWrapper<>();
        ew.eq("r.is_del", 0);
        if (StringUtils.isNotBlank(restartRecord.getDeviceNo())) {
            ew.eq("r.device_no", restartRecord.getDeviceNo());
        }
        if (StringUtils.isNotBlank(restartRecord.getType())) {
            ew.eq("r.type", restartRecord.getType());
        }
        if (restartRecord.getProjectId() != null) {
            ew.eq("r.project_id", restartRecord.getProjectId());
        }
        List<ProjectDeviceRestartRecord> list = baseMapper.getPageList(page, ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }
}
