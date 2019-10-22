package com.xywg.equipmentmonitor.modular.station.service.impl;

import com.xywg.equipmentmonitor.modular.station.model.ProjectMapStation;
import com.xywg.equipmentmonitor.modular.station.dao.ProjectMapStationMapper;
import com.xywg.equipmentmonitor.modular.station.service.IProjectMapStationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2019-03-20
 */
@Service
public class ProjectMapStationServiceImpl extends ServiceImpl<ProjectMapStationMapper, ProjectMapStation> implements IProjectMapStationService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByMapId(Integer mapId){
        baseMapper.deleteByMapId(mapId);
    }

}
