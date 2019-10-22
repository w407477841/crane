package com.xywg.equipmentmonitor.modular.device.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectElectricPowerHeartbeat;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentHeartbeat;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2018-09-29
 */
@Service
public interface ProjectElectricPowerHeartbeatService extends IService<ProjectElectricPowerHeartbeat> {

	ResultDTO<DataDTO<List<ProjectElectricPowerHeartbeat>>> selectPageList(
			RequestDTO<ProjectElectricPowerHeartbeat> res);


}
