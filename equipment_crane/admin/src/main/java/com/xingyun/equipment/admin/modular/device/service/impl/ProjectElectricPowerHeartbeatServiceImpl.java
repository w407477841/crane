package com.xingyun.equipment.admin.modular.device.service.impl;

import java.util.List;

import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectElectricPowerHeartbeat;
import com.xingyun.equipment.admin.modular.device.dao.ProjectElectricPowerHeartbeatMapper;
import com.xingyun.equipment.admin.modular.device.service.ProjectElectricPowerHeartbeatService;
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
