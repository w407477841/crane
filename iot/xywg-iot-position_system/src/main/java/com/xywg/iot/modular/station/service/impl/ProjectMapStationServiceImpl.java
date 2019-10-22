package com.xywg.iot.modular.station.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.modular.station.model.ProjectMapStation;
import com.xywg.iot.modular.station.dao.ProjectMapStationMapper;
import com.xywg.iot.modular.station.service.IProjectMapStationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2019-03-21
 */
@Service
public class ProjectMapStationServiceImpl extends ServiceImpl<ProjectMapStationMapper, ProjectMapStation> implements IProjectMapStationService {

    @Override
    public ProjectMapStation selectByDeviceId(Integer id,Integer stationNo) {

        Wrapper<ProjectMapStation> wrapper = new EntityWrapper<>();
        wrapper.eq("station_id",id);
        wrapper.eq("station_no",Integer.toString(stationNo));


        return this.selectOne(wrapper);
    }

    @Override
    public ProjectMapStation selectByDeviceId(Integer id) {

        Wrapper<ProjectMapStation> wrapper = new EntityWrapper<>();
        wrapper.eq("station_id",id);
        return this.selectOne(wrapper);
    }

}
