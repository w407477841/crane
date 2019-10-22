package com.xywg.equipment.monitor.iot.modular.envmon.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.envmon.dao.ProjectEnvironmentMonitorMapper;
import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentMonitor;
import com.xywg.equipment.monitor.iot.modular.envmon.service.IProjectEnvironmentMonitorService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-21
 */
@Service
public class ProjectEnvironmentMonitorServiceImpl extends ServiceImpl<ProjectEnvironmentMonitorMapper, ProjectEnvironmentMonitor> implements IProjectEnvironmentMonitorService {

    public final static String DEVICE_INFO_SUFF="deviceinfo:monitor";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XywgProerties xywgProerties;

    @Override
    public ProjectEnvironmentMonitor selectOne(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + ProjectEnvironmentMonitorServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        if(redisUtil.exists(key)){
            System.out.println("存在"+key);
            return (ProjectEnvironmentMonitor) redisUtil.get(key);
        }else{
            System.out.println("不存在"+key);
            Wrapper<ProjectEnvironmentMonitor> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no",deviceNo);
            wrapper.eq("is_del",0);
            ProjectEnvironmentMonitor  result = SqlHelper.getObject(baseMapper.selectList(wrapper));
            //存5分钟
            redisUtil.set(key,result,5L);
            return result;
        }
    }
}
