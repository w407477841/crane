package com.xywg.iot.modular.station.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.modular.station.model.ProjectDevice;
import com.xywg.iot.modular.station.model.ProjectDeviceStock;
import com.xywg.iot.modular.station.dao.ProjectDeviceStockMapper;
import com.xywg.iot.modular.station.service.IProjectDeviceService;
import com.xywg.iot.modular.station.service.IProjectDeviceStockService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProjectDeviceStockServiceImpl extends ServiceImpl<ProjectDeviceStockMapper, ProjectDeviceStock> implements IProjectDeviceStockService {
    @Autowired
    private IProjectDeviceService projectDeviceService;

    @Override
    public ProjectDevice selectStationByDeviceNo(String deviceNo) {
        Wrapper<ProjectDevice> wrapper  =new EntityWrapper<>();
        wrapper.eq("device_no",deviceNo);
        wrapper.eq("type",2);
        wrapper.eq("is_del",0);
        return projectDeviceService.selectOne(wrapper);
    }



    @Override
    public ProjectDevice selectTagByDeviceNo(String deviceNo) {
        Wrapper<ProjectDevice> wrapper  =new EntityWrapper<>();
        wrapper.eq("device_no",deviceNo);
        wrapper.eq("type",3);
        wrapper.eq("is_del",0);
        return projectDeviceService.selectOne(wrapper);
    }

}
