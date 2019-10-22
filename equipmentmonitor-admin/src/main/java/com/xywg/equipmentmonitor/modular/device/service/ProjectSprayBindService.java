package com.xywg.equipmentmonitor.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectSprayDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectSprayBind;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectSprayVO;
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
