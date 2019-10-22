package com.xywg.equipment.monitor.iot.modular.crane.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.crane.cache.CacheLoader;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCrane;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
public interface IProjectCraneService extends IService<ProjectCrane> {

    /**
     * 在缓存存5分钟，减少访问
     * @return 所有设备
     */
    List<Map<String,String>> selectAllDevice();

    /**
     * 查询 设备
     * @param deviceNo
     * @return
     */
    public ProjectCrane selectOne(String deviceNo);

    /**
     * 删除 缓存
     * @param deviceNo
     */
    void removeCache(String deviceNo);

    /**
     * 改变在线状态
     * @param tableName
     * @param deviceNo
     * @param status
     */
    public void doStatusChange(String tableName, String deviceNo, int status);

}
