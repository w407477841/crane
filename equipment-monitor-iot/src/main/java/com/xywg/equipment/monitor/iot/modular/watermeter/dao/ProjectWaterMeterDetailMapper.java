package com.xywg.equipment.monitor.iot.modular.watermeter.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.iot.modular.watermeter.model.ProjectWaterMeterDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-09-17
 */
public interface ProjectWaterMeterDetailMapper extends BaseMapper<ProjectWaterMeterDetail> {
    @Select("select t.create_time as createTime,t.current_degree current , t.actual_degree actualDegree  from t_project_water_meter_detail t where t.electric_id = #{eleId} order by t.create_time DESC limit 0,1")
    ProjectWaterMeterDetail getLastInfo(@Param("eleId") int eleId);

    @Select("select t.actual_degree as actualDegree  from t_project_water_meter_detail_${month} t where t.electric_id = #{eleId} and create_time like '${daily}%' order by create_time asc limit 0,1")
    BigDecimal getFirstInfo(@Param("month")String month , @Param("daily")String daily, @Param("eleId") int eleId);

    void create(@Param("t") ProjectWaterMeterDetail detail, @Param("tableName") String tableName);
}
