package com.xywg.equipment.monitor.modular.sjj.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.sjj.dao.ProjectLiftMapper;
import com.xywg.equipment.monitor.modular.sjj.model.ProjectLift;
import com.xywg.equipment.monitor.modular.sjj.service.IProjectLiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IProjectLiftServiceImpl extends ServiceImpl<ProjectLiftMapper, ProjectLift> implements IProjectLiftService {

    @Autowired
    private ProjectLiftMapper mapper;

    @Override
    public List<String> selectDeviceNoOfNeedDispatch() {
        List<String> result= mapper.selectDeviceNoOfNeedDispatch(1);
        return  result;
    }
}
