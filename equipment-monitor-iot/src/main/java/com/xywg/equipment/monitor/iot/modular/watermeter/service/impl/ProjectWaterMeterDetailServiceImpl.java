package com.xywg.equipment.monitor.iot.modular.watermeter.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.iot.modular.watermeter.dao.ProjectWaterMeterDetailMapper;
import com.xywg.equipment.monitor.iot.modular.watermeter.model.ProjectWaterMeterDetail;
import com.xywg.equipment.monitor.iot.modular.watermeter.service.IProjectWaterMeterDetailService;
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
