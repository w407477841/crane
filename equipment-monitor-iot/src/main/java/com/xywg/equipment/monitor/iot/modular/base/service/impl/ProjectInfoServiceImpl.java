package com.xywg.equipment.monitor.iot.modular.base.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.base.dao.ProjectInfoMapper;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectInfo;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Service
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements IProjectInfoService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XywgProerties xywgProerties;

    @Override
    public ProjectInfo selectCacheOne(Integer id) {
        String key = xywgProerties.getRedisHead() + ":project:id:" +id;
        if(redisUtil.exists(key)){

            return (ProjectInfo) redisUtil.get(key);
        }else{

            ProjectInfo  result =  this.selectById(id);
            //存5分钟
            redisUtil.set(key,result,60L);
            return result;
        }
    }

}
