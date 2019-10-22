package com.xywg.equipment.monitor.iot.modular.spray.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.iot.modular.spray.dao.ProjectSprayBindMapper;
import com.xywg.equipment.monitor.iot.modular.spray.dao.ProjectSprayDetailMapper;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSpray;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSprayBind;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSprayBindVo;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSprayDetail;
import com.xywg.equipment.monitor.iot.modular.spray.service.ProjectSprayBindService;
import com.xywg.equipment.monitor.iot.modular.spray.service.ProjectSprayDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Service
public class ProjectSprayBindServiceImpl extends ServiceImpl<ProjectSprayBindMapper, ProjectSprayBind> implements ProjectSprayBindService {

    @Override
    public List<ProjectSpray> getListProjectSprayBind(Integer environmentId) {
        return baseMapper.getListProjectSprayBind( environmentId);
    }
}
