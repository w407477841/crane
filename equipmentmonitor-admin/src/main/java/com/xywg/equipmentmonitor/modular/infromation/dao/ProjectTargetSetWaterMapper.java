package com.xywg.equipmentmonitor.modular.infromation.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.modular.device.model.ProjectWaterMeter;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectTargetSetWater;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-10-15
 */
public interface ProjectTargetSetWaterMapper extends BaseMapper<ProjectTargetSetWater> {
    /**
     * 获取列表
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectTargetSetWater> selectTargetSetWater(Page<ProjectTargetSetWater> page,Map<String, Object> map) throws Exception;

    /**
     * 占用
     * @param map
     * @return
     */
    Integer plusCallTimes(Map<String,Object> map);

    /**
     * 解除占用
     * @param projectWaterMeters
     * @return
     */
    Integer minusCallTimes(List<ProjectWaterMeter> projectWaterMeters);
}
