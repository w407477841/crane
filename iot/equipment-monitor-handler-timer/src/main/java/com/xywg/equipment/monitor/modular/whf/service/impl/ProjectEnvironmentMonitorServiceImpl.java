package com.xywg.equipment.monitor.modular.whf.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectEnvironmentMonitorMapper;
import com.xywg.equipment.monitor.modular.whf.dto.DeviceStatus;
import com.xywg.equipment.monitor.modular.whf.dto.DeviceStatusVO;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitor;
import com.xywg.equipment.monitor.modular.whf.service.IProjectEnvironmentMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public final static String DEVICE_INFO_SUFF="str:deviceinfo:monitor";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XywgProerties xywgProerties;

    @Override
    public ProjectEnvironmentMonitor selectOne(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + ProjectEnvironmentMonitorServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        if(redisUtil.exists(key)){
            System.out.println("存在"+key);
            String json = (String) redisUtil.get(key);
            return JSONUtil.toBean(json,ProjectEnvironmentMonitor.class);
        }else{
            System.out.println("不存在"+key);
            Wrapper<ProjectEnvironmentMonitor> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no",deviceNo);
            wrapper.eq("is_del",0);
            ProjectEnvironmentMonitor  result = SqlHelper.getObject(baseMapper.selectList(wrapper));
            //存5分钟
            redisUtil.set(key,JSONUtil.toJsonStr(result),5L);
            return result;
        }
    }

    @Override
    public List<DeviceStatusVO> deviceStatus() {
        return baseMapper.deviceStatus();
    }
}
