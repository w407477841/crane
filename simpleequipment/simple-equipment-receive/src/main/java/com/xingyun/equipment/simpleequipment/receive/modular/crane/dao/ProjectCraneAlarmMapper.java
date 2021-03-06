package com.xingyun.equipment.simpleequipment.receive.modular.crane.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xingyun.equipment.simpleequipment.receive.modular.crane.model.ProjectCraneAlarm;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
public interface ProjectCraneAlarmMapper extends BaseMapper<ProjectCraneAlarm> {
    /**
    *
    * @author: wangyifei
    * Description: 插入报警信息
    * Date: 8:48 2018/8/24
    * @param alarm  数据
    * @param tableName  表名
    */
void createAlarm(@Param("t") ProjectCraneAlarm alarm, @Param("tableName") String tableName);

}
