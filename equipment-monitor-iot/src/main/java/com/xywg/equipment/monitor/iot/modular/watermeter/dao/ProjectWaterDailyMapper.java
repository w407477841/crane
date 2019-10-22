package com.xywg.equipment.monitor.iot.modular.watermeter.dao;

import com.xywg.equipment.monitor.iot.modular.watermeter.model.ProjectWaterDaily;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 电表每日统计
正常情况   读数= 明细中最后一条数据 ， 用电量=（ 明细中最后一条数据 减去 每日统计最后一条）
第一次      读数= 明细中最后一条数据 ， 用电量=（ 明细中最后一条数据 减去 明细当日第一条数据）
没有数据  读数=（用电量为每日统计最后一个读数） ，用电量 = 0 Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-11-20
 */
public interface ProjectWaterDailyMapper extends BaseMapper<ProjectWaterDaily> {
    /**
     *
     * @param month
     * @param daily
     * @return
     */
    public List<Map<String,Object>> last(@Param("month") String month, @Param("daily") String daily,@Param("daily1") String daily1);

    /**
     * 一台设备的最后数据
     * @param month
     * @param daily
     * @param daily1
     * @param deviceId
     * @return
     */
    public List<Map<String,Object>> lastDevice(@Param("month") String month, @Param("daily") String daily,@Param("daily1") String daily1,@Param("deviceId")Integer deviceId);
}
