package com.xingyun.equipment.crane.modular.device.dao;

import com.xingyun.equipment.crane.modular.device.model.ProjectHelmetAlarm;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 人员健康异常报警信息 Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
public interface ProjectHelmetAlarmMapper extends BaseMapper<ProjectHelmetAlarm> {

    /**
     * 查询报警详情信息
     * @param month
     * @param helmetId
     * @param beginDate
     * @param endDate
     * @return
     */
    List<ProjectHelmetAlarm> getAlarmMessage(@Param("month") String month, @Param("helmetId") Integer helmetId, @Param("beginDate") String beginDate, @Param("endDate") String endDate);
}
