package com.xingyun.equipment.crane.modular.device.dao;

import com.xingyun.equipment.crane.modular.device.model.ProjectCraneAlarm;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2019-06-21
 */
public interface ProjectCraneAlarmMapper extends BaseMapper<ProjectCraneAlarm> {
    /**
     * 报警处理
     * @param alarm
     */
    void updateHandle(ProjectCraneAlarm alarm);
}
