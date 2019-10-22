package com.xywg.iot.modules.crane.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.common.global.GlobalStaticConstant;
import com.xywg.iot.common.utils.RedisUtil;
import com.xywg.iot.modules.crane.dao.ProjectCraneMapper;
import com.xywg.iot.modules.crane.model.ProjectCrane;
import com.xywg.iot.modules.crane.service.IProjectCraneService;
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
    private final String ALL_STR_SUFF = "device:all";
    public final static String DEVICE_INFO_SUFF = "deviceinfo:crane";
    @Autowired
    private RedisUtil redisUtil;


    @Override
    public List<Map<String, String>> selectAllDevice() {
        /*String key = xywgProerties.getRedisHead() + ":" + this.ALL_STR_SUFF;
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
        }*/
        return null;
    }

    @Override
    public ProjectCrane selectOne(String deviceNo) {
        String key = GlobalStaticConstant.REDIS_PREFIX + ":" + ProjectCraneServiceImpl.DEVICE_INFO_SUFF + ":" + deviceNo;
        if (redisUtil.exists(key)) {
            return (ProjectCrane) redisUtil.get(key);
        } else {
            Wrapper<ProjectCrane> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no", deviceNo);
            wrapper.eq("is_del", 0);
            List<ProjectCrane> result = baseMapper.selectList(wrapper);
            if (result != null && result.size() > 0) {
                redisUtil.set(key, result.get(0), 5L);
                return result.get(0);
            }
        }
        return null;
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
