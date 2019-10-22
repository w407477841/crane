package com.xywg.equipment.monitor.modular.whf.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectEnvironmentMonitorMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitor;
import com.xywg.equipment.monitor.modular.whf.service.IProjectEnvironmentMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectEnvironmentMonitorServiceImpl.class);

    public final static String DEVICE_INFO_SUFF="str:deviceinfo:monitor";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XywgProerties xywgProerties;

    @Override
    public ProjectEnvironmentMonitor selectOne(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + ProjectEnvironmentMonitorServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        if(redisUtil.exists(key)){
            LOGGER.debug("存在缓存: <"+key+">");
            String json = (String) redisUtil.get(key);
            return JSONUtil.toBean(json,ProjectEnvironmentMonitor.class);
        }else{
            LOGGER.debug("不存在缓存: <"+key+">");
            Wrapper<ProjectEnvironmentMonitor> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no",deviceNo);
            wrapper.eq("is_del",0);

            ProjectEnvironmentMonitor  result = null;
            //查询所有该设备号的设备
            List<ProjectEnvironmentMonitor> list= this.selectList(wrapper);
            if(list == null||list.size()==0){
                return null;
            }
            //找到启用的设备
            for(ProjectEnvironmentMonitor monitor:list){
                if(new Integer(1).equals(monitor.getStatus())){
                    result = monitor;
                    break;
                }
            }
            //未找到启用设备，设备最新的一条为当前设备
            if(result==null){
                result = list.get(list.size()-1);
            }
                //存5分钟
            redisUtil.set(key,JSONUtil.toJsonStr(result),5L);
            return result;
        }
    }
}
