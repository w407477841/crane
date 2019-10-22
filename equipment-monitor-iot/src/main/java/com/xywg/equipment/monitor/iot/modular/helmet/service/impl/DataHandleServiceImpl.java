package com.xywg.equipment.monitor.iot.modular.helmet.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.global.GlobalStaticConstant;
import com.xywg.equipment.monitor.iot.modular.helmet.model.DataDomain;
import com.xywg.equipment.monitor.iot.modular.helmet.model.ProjectHelmet;
import com.xywg.equipment.monitor.iot.modular.helmet.service.DataHandleService;
import com.xywg.equipment.monitor.iot.modular.helmet.service.ProjectHelmetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hjy
 * @date 2018/11/22
 */
@Service
public class DataHandleServiceImpl implements DataHandleService {
    @Autowired
    private ProjectHelmetService projectHelmetService;


    @Override
    public void terminalDown(DataDomain dataDomain) {
        ProjectHelmet projectHelmet = new ProjectHelmet();
        projectHelmet.setImei(dataDomain.getImei());
        projectHelmet.setIsOnline(0);
        Wrapper<ProjectHelmet> wrapper = new EntityWrapper<>();
        wrapper.eq("imei", dataDomain.getImei());
        wrapper.eq("is_del", 0);
        projectHelmetService.update(projectHelmet, wrapper);
    }

    @Override
    public void login(DataDomain dataDomain) {
        ProjectHelmet projectHelmet = new ProjectHelmet();
        projectHelmet.setImei(dataDomain.getImei());
        projectHelmet.setIsOnline(1);
        Wrapper<ProjectHelmet> wrapper = new EntityWrapper<>();
        wrapper.eq("imei", dataDomain.getImei());
        wrapper.eq("status", 1);
        wrapper.eq("is_del", 0);
        projectHelmetService.update(projectHelmet, wrapper);
    }

    @Override
    public void positionHealth(DataDomain dataDomain) {
        if (dataDomain.getPositionHealthCommand() == GlobalStaticConstant.POSITION) {
            dealPosition(dataDomain);
        }else if(dataDomain.getPositionHealthCommand() == GlobalStaticConstant.HEALTH) {
             dealHealth(dataDomain);
        }
    }

    /**
     * 定位数据
     * @param dataDomain
     */
    private void dealPosition(DataDomain dataDomain) {

    }

    /**
     * 健康数据
     * @param dataDomain
     */
    private void dealHealth( DataDomain dataDomain) {

    }

}
