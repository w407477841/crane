package com.xywg.equipment.monitor.modular.tj.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.tj.dao.ProjectCraneMapper;
import com.xywg.equipment.monitor.modular.tj.model.ProjectCrane;
import com.xywg.equipment.monitor.modular.tj.service.IProjectCraneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IProjectCraneServiceImpl extends ServiceImpl<ProjectCraneMapper, ProjectCrane>
implements IProjectCraneService
{
    @Autowired
    private ProjectCraneMapper mapper;

    @Override
    public List<String> selectDeviceNoOfNeedDispatch() {
        List<String> result= mapper.selectDeviceNoOfNeedDispatch(1);
        return result;
    }
}
