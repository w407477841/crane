package com.xingyun.equipment.crane.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.dto.ProjectSprayDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectSprayBind;
import com.xingyun.equipment.crane.modular.device.vo.ProjectSprayVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 20:00
 */
@Service
public interface ProjectSprayBindService extends IService<ProjectSprayBind>{

    ResultDTO<DataDTO<List<ProjectSprayVO>>> getList(Integer id);

    boolean add(ProjectSprayDTO projectSprayDTO);
}
