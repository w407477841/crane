package com.xywg.equipment.monitor.modular.whf.service;


import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.whf.model.ProjectCraneAlarm;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
public interface IProjectCraneAlarmService extends IService<ProjectCraneAlarm> {
    /**
     * 新增
     * @author: wangyifei
     * Description: 插入报警信息
     * Date: 8:48 2018/8/24
     * @param alarm  数据
     * @param tableName  表名
     */
    void createAlarm(@Param("t") ProjectCraneAlarm alarm, @Param("tableName") String tableName);
}
