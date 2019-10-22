package com.xywg.equipment.monitor.iot.modular.ammeter.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPower;
import com.xywg.equipment.monitor.iot.modular.ammeter.dao.ProjectElectricPowerMapper;
import com.xywg.equipment.monitor.iot.modular.ammeter.service.IProjectElectricPowerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentHeartbeat;
import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentMonitor;
import com.xywg.equipment.monitor.iot.modular.envmon.service.impl.ProjectEnvironmentMonitorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
@Service
public class ProjectElectricPowerServiceImpl extends ServiceImpl<ProjectElectricPowerMapper, ProjectElectricPower> implements IProjectElectricPowerService {

    public final static String DEVICE_INFO_SUFF="deviceinfo:ameter";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XywgProerties xywgProerties;

    @Override
    public ProjectElectricPower getBaseInfo(String deviceNo) {

        String key = xywgProerties.getRedisHead() + ":" + DEVICE_INFO_SUFF+":"+deviceNo;
        if(redisUtil.exists(key)){
            System.out.println("存在"+key);
            return (ProjectElectricPower) redisUtil.get(key);
        }else{
            System.out.println("不存在"+key);
            Wrapper<ProjectElectricPower> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no",deviceNo);
            wrapper.eq("is_del",0);
            ProjectElectricPower  result = SqlHelper.getObject(baseMapper.selectList(wrapper));
            //存5分钟
            redisUtil.set(key,result,5L);
            return result;
        }
    }

}
