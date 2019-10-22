package com.xywg.equipment.monitor.iot.modular.ammeter.dao;

import com.xywg.equipment.monitor.iot.modular.ammeter.model.ProjectElectricPowerDetail;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
public interface ProjectElectricPowerDetailMapper extends BaseMapper<ProjectElectricPowerDetail> {
    @Select("select t.create_time as createTime,t.current_degree as current, t.actual_degree as actualDegree  from t_project_electric_power_detail t where t.electric_id = #{eleId} order by t.create_time DESC limit 0,1")
    ProjectElectricPowerDetail getLastInfo(@Param("eleId") int eleId);
    @Select("select t.actual_degree as actualDegree  from t_project_electric_power_detail_${month} t where t.electric_id = #{eleId} and create_time like '${daily}%' order by create_time asc limit 0,1")
    BigDecimal getFirstInfo(@Param("month")String month ,@Param("daily")String daily,@Param("eleId") int eleId);

    void create(@Param("t") ProjectElectricPowerDetail detail, @Param("tableName") String tableName);

    BigDecimal getYearOnYear(@Param("tableName") String tableName,@Param("theTime") String theTime);

    BigDecimal getRingRatio(@Param("tableName") String tableName,@Param("theTime") String theTime);

}
