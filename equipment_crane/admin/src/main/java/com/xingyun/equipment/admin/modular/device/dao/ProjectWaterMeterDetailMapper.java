package com.xingyun.equipment.admin.modular.device.dao;

import com.xingyun.equipment.admin.modular.device.model.ProjectWaterMeterDetail;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-09-27
 */
public interface ProjectWaterMeterDetailMapper extends BaseMapper<ProjectWaterMeterDetail> {





    public Double getOneDaydegree(Map<String,Object> param);

    /**
     *  合计传入日期后用电合计
     * @param uuid  项目ID
     * @param type  水表类型
     * @param date  日期
     * @return
     */
    @Select("\tselect sum(amount_used) used from t_project_water_daily where device_id in (\n" +
            "\tselect id from t_project_water_meter where project_id in(\n" +
            "\t\tselect id from t_project_info where uuid = #{uuid}\n" +
            "\t\t) and type = #{type}\n" +
            "\t\t) and statistics_date >='${date}' ")
     Double sum(@Param("uuid") String uuid,@Param("date") String date,@Param("type") int type);


    List<Map<String,Object>> eveSum(@Param("uuid") String uuid,@Param("date") String date,@Param("type") Integer type);
}
