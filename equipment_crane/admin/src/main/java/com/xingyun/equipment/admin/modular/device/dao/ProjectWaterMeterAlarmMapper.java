package com.xingyun.equipment.admin.modular.device.dao;

import com.xingyun.equipment.admin.modular.device.model.ProjectWaterMeterAlarm;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-09-27
 */
public interface ProjectWaterMeterAlarmMapper extends BaseMapper<ProjectWaterMeterAlarm> {
    /**
    * @author: wangyifei
    * Description:  查询项目下 异常设备数
    * Date: 14:37 2018/10/22
    */
    @Select("SELECT\n" +
            "\tcount(1) count\n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\t1\n" +
            "\t\tFROM\n" +
            "\t\t\tt_project_water_meter_alarm_201810\n" +
            "\t\tWHERE\n" +
            "\t\t\telectric_id IN (\n" +
            "\t\t\t\tSELECT\n" +
            "\t\t\t\t\tid\n" +
            "\t\t\t\tFROM\n" +
            "\t\t\t\t\tt_project_water_meter\n" +
            "\t\t\t\tWHERE\n" +
            "\t\t\t\t\tproject_id = (\n" +
            "\t\t\t\t\t\tSELECT\n" +
            "\t\t\t\t\t\t\tid\n" +
            "\t\t\t\t\t\tFROM\n" +
            "\t\t\t\t\t\t\tt_project_info\n" +
            "\t\t\t\t\t\tWHERE\n" +
            "\t\t\t\t\t\t\tuuid = #{uuid}\n" +
            "\t\t\t\t\t)\n" +
            "\t\t\t)\n" +
            "\t\tGROUP BY\n" +
            "\t\t\telectric_id\n" +
            "\t) m")
     int shuiDianExs(@Param("month") String month,@Param("uuid") String uuid);

}
