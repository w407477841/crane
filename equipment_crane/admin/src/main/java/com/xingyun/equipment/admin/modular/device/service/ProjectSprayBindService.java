package com.xingyun.equipment.admin.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.dto.ProjectSprayDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectSprayBind;
import com.xingyun.equipment.admin.modular.device.vo.ProjectSprayVO;
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
