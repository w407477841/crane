package com.xywg.equipment.monitor.iot.modular.watermeter.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.watermeter.dao.ProjectWaterMeterMapper;
import com.xywg.equipment.monitor.iot.modular.watermeter.model.ProjectWaterMeter;
import com.xywg.equipment.monitor.iot.modular.watermeter.service.IProjectWaterMeterService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProjectWaterMeterServiceImpl extends ServiceImpl<ProjectWaterMeterMapper, ProjectWaterMeter> implements IProjectWaterMeterService {

    public final static String DEVICE_INFO_SUFF="deviceinfo:water";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XywgProerties xywgProerties;

    @Override
    public ProjectWaterMeter getBaseInfo(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + DEVICE_INFO_SUFF+":"+deviceNo;
        if(redisUtil.exists(key)){
            System.out.println("存在"+key);
            return (ProjectWaterMeter) redisUtil.get(key);
        }else{
            System.out.println("不存在"+key);
            Wrapper<ProjectWaterMeter> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no",deviceNo);
            wrapper.eq("is_del",0);
            ProjectWaterMeter  result = SqlHelper.getObject(baseMapper.selectList(wrapper));
            //存5分钟
            redisUtil.set(key,result,5L);
            return result;
        }
    }
}
