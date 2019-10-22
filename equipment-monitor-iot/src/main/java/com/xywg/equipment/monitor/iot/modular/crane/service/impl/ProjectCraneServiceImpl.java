package com.xywg.equipment.monitor.iot.modular.crane.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneMapper;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCrane;
import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Service
public class ProjectCraneServiceImpl extends ServiceImpl<ProjectCraneMapper, ProjectCrane> implements IProjectCraneService {
    private final  String ALL_STR_SUFF = "device:all";
    public final static String DEVICE_INFO_SUFF ="deviceinfo:crane";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XywgProerties xywgProerties;

    @Override
    public List<Map<String,String>> selectAllDevice() {
        String key = xywgProerties.getRedisHead() + ":" + this.ALL_STR_SUFF;
        if (redisUtil.exists(key)) {
            System.out.println("#####从Redis读取了所有设备信息");
            return (List<Map<String,String>>) redisUtil.get(key);
        } else {
            System.out.println("#####从MySQL读取了所有设备信息");
            List<Map<String,String>> result = baseMapper.selectAllDevice();
            redisUtil.set(
                    key,
                    result,
                    5L
            );
            return result;
        }
    }
    @Override
    public ProjectCrane selectOne(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + ProjectCraneServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        if(redisUtil.exists(key)){

            return (ProjectCrane) redisUtil.get(key);
        }else{

            Wrapper<ProjectCrane> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no",deviceNo);
            wrapper.eq("is_del",0);

            ProjectCrane  result =  SqlHelper.getObject(baseMapper.selectList(wrapper));
            //存5分钟
            redisUtil.set(key,result,5L);
            return result;
        }



    }
    @Override
    public void removeCache(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + ProjectCraneServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        redisUtil.remove(key);
    }
    /**
     * 改变在线状态
     * @param tableName
     * @param deviceNo
     * @param status
     */
    @Override
    public void doStatusChange(String tableName,String deviceNo,int status){
        baseMapper.doDeviceOnlineChange(tableName,deviceNo,status);
    }

}
