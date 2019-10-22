package com.xingyun.equipment.timer.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.timer.model.ProjectCrane;

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


}
