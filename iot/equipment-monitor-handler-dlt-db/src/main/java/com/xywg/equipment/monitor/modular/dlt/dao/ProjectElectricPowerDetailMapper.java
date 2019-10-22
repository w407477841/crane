package com.xywg.equipment.monitor.modular.dlt.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.dlt.model.ProjectElectricPowerDetail;
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
    @Select("select t.create_time as createTime,t.current_degree as current from t_project_electric_power_detail t where t.electric_id = #{eleId} order by t.create_time DESC limit 0,1")
    ProjectElectricPowerDetail getLastInfo(@Param("eleId") int eleId);

    void create(@Param("t") ProjectElectricPowerDetail detail, @Param("tableName") String tableName);

    BigDecimal getYearOnYear(@Param("tableName") String tableName, @Param("theTime") String theTime);

    BigDecimal getRingRatio(@Param("tableName") String tableName, @Param("theTime") String theTime);

}
