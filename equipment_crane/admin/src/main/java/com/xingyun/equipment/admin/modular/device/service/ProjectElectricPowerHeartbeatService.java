package com.xingyun.equipment.admin.modular.device.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectElectricPowerHeartbeat;
import com.xingyun.equipment.admin.modular.device.model.ProjectEnvironmentHeartbeat;
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
