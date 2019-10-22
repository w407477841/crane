package com.xingyun.equipment.timer.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.timer.config.properties.XywgProerties;
import com.xingyun.equipment.timer.util.RedisUtil;
import com.xingyun.equipment.timer.dao.ProjectCraneMapper;
import com.xingyun.equipment.timer.model.ProjectCrane;
import com.xingyun.equipment.timer.service.IProjectCraneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Service
@SuppressWarnings("all")
public class ProjectCraneServiceImpl extends ServiceImpl<ProjectCraneMapper, ProjectCrane> implements IProjectCraneService {
    private final  String ALL_STR_SUFF = "device:all";
    public final static String DEVICE_INFO_SUFF ="deviceinfo:crane";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XywgProerties xywgProerties;

    @Override
    public ProjectCrane selectOne(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + ProjectCraneServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        if(redisUtil.exists(key)){

            return  JSONUtil.toBean((String)redisUtil.get(key),ProjectCrane.class) ;
        }else{

            Wrapper<ProjectCrane> wrapper = new EntityWrapper();
            wrapper.eq("device_no",deviceNo);
            wrapper.eq("is_del",0);

            ProjectCrane  result =  SqlHelper.getObject(baseMapper.selectList(wrapper));
            if(result==null) {return null;}
            //存5分钟
            redisUtil.set(key,JSONUtil.toJsonStr(result),5L);
            return result;
        }

    }

    @Override
    public void removeCache(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + ProjectCraneServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        redisUtil.remove(key);
    }

}
