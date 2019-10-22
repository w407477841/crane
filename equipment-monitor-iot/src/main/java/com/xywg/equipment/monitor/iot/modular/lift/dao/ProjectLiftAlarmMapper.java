package com.xywg.equipment.monitor.iot.modular.lift.dao;

import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLiftAlarm;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-22
 */
public interface ProjectLiftAlarmMapper extends BaseMapper<ProjectLiftAlarm> {
/**
* @author: wangyifei
* Description: 插入 报警数据
* Date: 8:57 2018/8/24
 * @param tableName 表名
 * @param alarm  数据
*/
void createAlarm(@Param("t") ProjectLiftAlarm alarm,@Param("tableName") String tableName);
}
