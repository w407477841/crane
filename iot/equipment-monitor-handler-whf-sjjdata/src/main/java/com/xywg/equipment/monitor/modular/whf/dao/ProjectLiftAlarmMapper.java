package com.xywg.equipment.monitor.modular.whf.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftAlarm;
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
     * 新增
     * @author: wangyifei
     * Description: 插入 报警数据
     * Date: 8:57 2018/8/24
     * * @param tableName 表名
     * @param alarm  数据
     */
    void createAlarm(@Param("t") ProjectLiftAlarm alarm,@Param("tableName") String tableName);
}
