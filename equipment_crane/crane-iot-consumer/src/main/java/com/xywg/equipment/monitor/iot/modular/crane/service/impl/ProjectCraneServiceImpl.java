package com.xywg.equipment.monitor.iot.modular.crane.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.crane.cache.CacheLoader;
import com.xingyun.crane.cache.CacheTemplateService;
import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xingyun.crane.cache.RedisUtil;;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneMapper;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCrane;
import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@SuppressWarnings("all")
public class ProjectCraneServiceImpl extends ServiceImpl<ProjectCraneMapper, ProjectCrane> implements IProjectCraneService {
    private final String ALL_STR_SUFF = "device:all";
    public final static String DEVICE_INFO_SUFF = "deviceinfo:crane";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XywgProerties xywgProerties;
    @Autowired
    private CacheTemplateService cacheTemplateService;

    @Override
    public List<Map<String, String>> selectAllDevice() {
        String key = xywgProerties.getRedisHead() + ":" + this.ALL_STR_SUFF;
        if (redisUtil.exists(key)) {
            System.out.println("#####从Redis读取了所有设备信息");
            return (List<Map<String, String>>) redisUtil.get(key);
        } else {
            System.out.println("#####从MySQL读取了所有设备信息");
            List<Map<String, String>> result = baseMapper.selectAllDevice();
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
        String key = xywgProerties.getRedisHead() + ":" + ProjectCraneServiceImpl.DEVICE_INFO_SUFF + ":" + deviceNo;

        return cacheTemplateService.findData(key, 5L, ProjectCrane.class, new CacheLoader<ProjectCrane>() {
            @Override
            public ProjectCrane load() {
                Wrapper<ProjectCrane> wrapper = new EntityWrapper<>();
                wrapper.eq("device_no", deviceNo);
                wrapper.eq("is_del", 0);
                wrapper.eq("status", 1);
                ProjectCrane result = SqlHelper.getObject(baseMapper.selectList(wrapper));

                return result;
            }
        });
    }

    @Override
    public void removeCache(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + ProjectCraneServiceImpl.DEVICE_INFO_SUFF + ":" + deviceNo;
        redisUtil.remove(key);
    }

    /**
     * 改变在线状态
     *
     * @param tableName
     * @param deviceNo
     * @param status
     */
    @Override
    public void doStatusChange(String tableName, String deviceNo, int status) {
        baseMapper.doDeviceOnlineChange(tableName, deviceNo, status);
    }


}
