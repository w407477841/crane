package com.xingyun.equipment.crane.modular.device.service;

import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectHelmetAlarm;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 人员健康异常报警信息 服务类
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
public interface IProjectHelmetAlarmService extends IService<ProjectHelmetAlarm> {

    /**
     * 按报警类型统计
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectHelmetAlarm>>> getAlarmMessage(RequestDTO request);

    /**
     * 查询报警详情信息
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectHelmetAlarm>>> getListByHelmetId(RequestDTO request);
}
