package com.xingyun.equipment.crane.modular.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.dao.ProjectSprayDetailMapper;
import com.xingyun.equipment.crane.modular.device.model.ProjectSprayDetail;
import com.xingyun.equipment.crane.modular.device.service.ProjectSprayDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 10:54
 */
@Service
public class ProjectSprayDetailServiceImpl extends ServiceImpl<ProjectSprayDetailMapper,ProjectSprayDetail> implements ProjectSprayDetailService{
    @Override
    public ResultDTO<DataDTO<List<ProjectSprayDetail>>> getBySprayId(RequestDTO request) {
        Page<ProjectSprayDetail> page = new Page<ProjectSprayDetail>(request.getPageNum(), request.getPageSize());
        EntityWrapper<RequestDTO> ew = new EntityWrapper<RequestDTO>();
        ew.eq("is_del", 0);
        ew.eq("spray_id", request.getId());
        List<ProjectSprayDetail> list = baseMapper.getBySprayId(page, ew);
        return new ResultDTO(true, DataDTO.factory(list, page.getTotal()));
    }
}
