package com.xywg.equipment.monitor.iot.modular.ammeter.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectAmmeterDaily;
import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPower;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 电表每日统计 Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-11-20
 */
public interface ProjectAmmeterDailyMapper extends BaseMapper<ProjectAmmeterDaily> {
    /**
     *
     * @param month
     * @param daily
     * @return
     */
    public List<Map<String,Object>> last(@Param("month") String month,@Param("daily") String daily,@Param("daily1") String daily1);

    /**
     * 一台设备的最后数据
     * @param month
     * @param daily
     * @param daily1
     * @param deviceId
     * @return
     */
    public List<Map<String,Object>> lastDevice(@Param("month") String month,@Param("daily") String daily,@Param("daily1") String daily1,@Param("deviceId") Integer deviceId);
}
