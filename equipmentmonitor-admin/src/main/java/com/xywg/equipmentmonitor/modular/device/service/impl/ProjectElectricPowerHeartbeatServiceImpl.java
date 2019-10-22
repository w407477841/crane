package com.xywg.equipmentmonitor.modular.device.service.impl;

import java.util.List;

import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectElectricPowerHeartbeat;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectElectricPowerHeartbeatMapper;
import com.xywg.equipmentmonitor.modular.device.service.ProjectElectricPowerHeartbeatService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-09-29
 */
@Service
public class ProjectElectricPowerHeartbeatServiceImpl extends ServiceImpl<ProjectElectricPowerHeartbeatMapper, ProjectElectricPowerHeartbeat> implements ProjectElectricPowerHeartbeatService {

	@Override
	public ResultDTO<DataDTO<List<ProjectElectricPowerHeartbeat>>> selectPageList(RequestDTO<ProjectElectricPowerHeartbeat> res) {
        Page<ProjectElectricPowerHeartbeat> page = new Page<>(res.getPageNum(), res.getPageSize());
        List<ProjectElectricPowerHeartbeat> list = baseMapper.selectPageList(page,res.getBody());
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

	

}
