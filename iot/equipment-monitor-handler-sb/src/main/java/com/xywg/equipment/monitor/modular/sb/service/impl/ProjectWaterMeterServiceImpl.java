package com.xywg.equipment.monitor.modular.sb.service.impl;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.sb.dao.ProjectWaterMeterMapper;
import com.xywg.equipment.monitor.modular.sb.model.ProjectWaterMeter;
import com.xywg.equipment.monitor.modular.sb.service.IProjectWaterMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-09-17
 */
@Service
public class ProjectWaterMeterServiceImpl extends ServiceImpl<ProjectWaterMeterMapper, ProjectWaterMeter> implements IProjectWaterMeterService {

    public final static String DEVICE_INFO_SUFF="str:deviceinfo:water";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XywgProerties xywgProerties;

    @Override
    public ProjectWaterMeter getBaseInfo(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + DEVICE_INFO_SUFF+":"+deviceNo;
        if(redisUtil.exists(key)){
            String json = (String) redisUtil.get(key);
            return JSONUtil.toBean(json,ProjectWaterMeter.class);
        }else{
            System.out.println("不存在"+key);
            Wrapper<ProjectWaterMeter> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no",deviceNo);
            wrapper.eq("is_del",0);
            ProjectWaterMeter  result = null;
            List<ProjectWaterMeter> projectWaterMeters = this.selectList(wrapper);
            if(projectWaterMeters == null || projectWaterMeters.size() == 0) {
                return null;
            }
            for(ProjectWaterMeter projectWaterMeter : projectWaterMeters) {
                if(new Integer(1).equals(projectWaterMeter.getStatus())) {
                    result = projectWaterMeter;
                    break;
                }
            }
            if(result == null) {
                result = projectWaterMeters.get(projectWaterMeters.size() - 1);
            }
            //存5分钟
            redisUtil.set(key, JSONUtil.toJsonStr(result),5L);
            return result;
        }
    }
}
