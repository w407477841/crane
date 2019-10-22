package com.xywg.equipment.monitor.modular.sb.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.sb.model.ProjectWaterMeterDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-09-17
 */
public interface ProjectWaterMeterDetailMapper extends BaseMapper<ProjectWaterMeterDetail> {
    @Select("select t.create_time as createTime,t.current from t_project_water_meter_detail t where t.electric_id = #{eleId} order by t.create_time DESC limit 0,1")
    ProjectWaterMeterDetail getLastInfo(@Param("eleId") int eleId);

    void create(@Param("t") ProjectWaterMeterDetail detail, @Param("tableName") String tableName);
}
