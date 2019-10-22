package com.xingyun.equipment.admin.modular.device.service;

import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectCraneAlarm;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2019-06-21
 */
public interface IProjectCraneAlarmService extends IService<ProjectCraneAlarm> {
    /**
     * 处理
     * @param alarm
     * @return
     */
    ResultDTO<Object> updateHandle(ProjectCraneAlarm alarm);
}
