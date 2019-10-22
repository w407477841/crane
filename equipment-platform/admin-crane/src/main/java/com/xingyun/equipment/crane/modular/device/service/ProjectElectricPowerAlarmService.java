package com.xingyun.equipment.crane.modular.device.service;

import java.util.List;

import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.baseinfo.model.ProjectUser;
import com.xingyun.equipment.crane.modular.device.dto.CountAlarmByDeviceNo;
import com.xingyun.equipment.crane.modular.device.dto.ProjectEnvironmentMessageDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectElectricPowerAlarm;
import com.xingyun.equipment.crane.modular.infromation.model.ProjectMessageModel;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2018-09-28
 */
public interface ProjectElectricPowerAlarmService extends IService<ProjectElectricPowerAlarm> {

	ResultDTO<DataDTO<List<ProjectElectricPowerAlarm>>> selectPageList(
            RequestDTO<ProjectElectricPowerAlarm> res);

	ResultDTO<List<CountAlarmByDeviceNo>> countAlarmByDeviceNo(
            RequestDTO<CountAlarmByDeviceNo> res);

	ResultDTO<DataDTO<List<ProjectElectricPowerAlarm>>> countAlarmByDeviceNoDetail(
            RequestDTO<CountAlarmByDeviceNo> res);

	ResultDTO<List<ProjectUser>> getWorkListByMonitorId(Integer monitorId);

	ResultDTO handleSMSSubmit(
            RequestDTO<ProjectEnvironmentMessageDTO> requestDTO);

	ResultDTO<List<ProjectMessageModel>> getSMSModel(
            RequestDTO<ProjectMessageModel> res);

}
