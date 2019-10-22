package com.xingyun.equipment.admin.modular.device.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.dao.ProjectCraneAlarmMapper;
import com.xingyun.equipment.admin.modular.device.model.ProjectCraneAlarm;
import com.xingyun.equipment.admin.modular.device.service.IProjectCraneAlarmService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2019-06-21
 */
@Service
public class ProjectCraneAlarmServiceImpl extends ServiceImpl<ProjectCraneAlarmMapper, ProjectCraneAlarm> implements IProjectCraneAlarmService {


    @Override
    public ResultDTO<Object> updateHandle(ProjectCraneAlarm alarm) {
        baseMapper.updateHandle(alarm);
        return new ResultDTO<>(true,null,"处理成功");
    }
}
