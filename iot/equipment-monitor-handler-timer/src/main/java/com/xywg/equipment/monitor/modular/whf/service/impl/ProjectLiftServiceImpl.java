package com.xywg.equipment.monitor.modular.whf.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectLiftMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLift;
import com.xywg.equipment.monitor.modular.whf.service.IProjectLiftService;
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
    private XywgProerties xywgProerties;
    public final static String DEVICE_INFO_SUFF = "str:deviceinfo:lift";
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ProjectLift selectOne(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + ProjectLiftServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        if(redisUtil.exists(key)){
            String json = (String)redisUtil.get(key);
            return JSONUtil.toBean(json,ProjectLift.class);
        }else{
            Wrapper<ProjectLift> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no",deviceNo);
            wrapper.eq("is_del",0);
            ProjectLift  result =  SqlHelper.getObject(baseMapper.selectList(wrapper));
            //存5分钟
            if(result != null) {
                redisUtil.set(key,JSONUtil.toJsonStr(result),5L);
            }
            return result;
        }
    }
}
