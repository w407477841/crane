package com.xywg.equipment.monitor.modular.sb.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.sb.dao.ProjectWaterMeterDetailMapper;
import com.xywg.equipment.monitor.modular.sb.model.ProjectWaterMeterDetail;
import com.xywg.equipment.monitor.modular.sb.service.IProjectWaterMeterDetailService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-09-17
 */
@Service
public class ProjectWaterMeterDetailServiceImpl extends ServiceImpl<ProjectWaterMeterDetailMapper, ProjectWaterMeterDetail> implements IProjectWaterMeterDetailService {
    @Override
    public ProjectWaterMeterDetail getLastInfo(int eleId) {

        return baseMapper.getLastInfo(eleId);
    }
}
