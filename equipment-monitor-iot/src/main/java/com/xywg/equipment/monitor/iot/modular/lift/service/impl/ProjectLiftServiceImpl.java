package com.xywg.equipment.monitor.iot.modular.lift.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.lift.dao.ProjectLiftMapper;
import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLift;
import com.xywg.equipment.monitor.iot.modular.lift.service.IProjectLiftService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-22
 */
@Service
public class ProjectLiftServiceImpl extends ServiceImpl<ProjectLiftMapper, ProjectLift> implements IProjectLiftService {
@Autowired
    private XywgProerties  xywgProerties;
    public final static String DEVICE_INFO_SUFF = "deviceinfo:lift";
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ProjectLift selectOne(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + ProjectLiftServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        if(redisUtil.exists(key)){
            return (ProjectLift) redisUtil.get(key);
        }else{
            Wrapper<ProjectLift> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no",deviceNo);
            wrapper.eq("is_del",0);

            ProjectLift  result =  SqlHelper.getObject(baseMapper.selectList(wrapper));
            //存5分钟
            redisUtil.set(key,result,5L);
            return result;
        }
    }
}
