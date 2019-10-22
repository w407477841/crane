package com.xingyun.equipment.crane.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectSprayDetail;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 10:54
 */
@Service
public interface ProjectSprayDetailService extends IService<ProjectSprayDetail>{

    ResultDTO<DataDTO<List<ProjectSprayDetail>>> getBySprayId(RequestDTO request);
}
