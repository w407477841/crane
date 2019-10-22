package com.xywg.equipmentmonitor.modular.device.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectSprayBindMapper;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectSprayDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectSprayBind;
import com.xywg.equipmentmonitor.modular.device.service.ProjectSprayBindService;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectSprayVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 19:59
 */
@Service
public class ProjectSprayBindServiceImpl extends ServiceImpl<ProjectSprayBindMapper,ProjectSprayBind> implements ProjectSprayBindService{
    @Override
    public ResultDTO<DataDTO<List<ProjectSprayVO>>> getList(Integer id) {
        return new ResultDTO(true, DataDTO.factory(baseMapper.getList(id)));
    }

    @Override
    @Transactional
    public boolean add(ProjectSprayDTO projectSprayDTO) {
        baseMapper.deleteByEnvironment(projectSprayDTO.getId());
        if(projectSprayDTO.getSpray().size()>0){
            for(int i=0;i<projectSprayDTO.getSpray().size();i++){
                ProjectSprayBind b=new ProjectSprayBind();
                b.setEnvironmentId(projectSprayDTO.getId());
                b.setProjectId(projectSprayDTO.getSpray().get(i).getProjectId());
                b.setSprayId(projectSprayDTO.getSpray().get(i).getId());
                b.setIsDel(0);
                baseMapper.add(b);
            }
        }
        return true;
    }
}
