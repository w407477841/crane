package com.xywg.equipment.monitor.modular.whf.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftAlarm;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-22
 */
public interface IProjectLiftAlarmService extends IService<ProjectLiftAlarm> {

    /**
     * 新增
     * @author: wangyifei
     * Description: 插入 报警数据
     * Date: 8:57 2018/8/24
     * * @param tableName 表名
     * @param alarm  数据
     */
    void createAlarm(@Param("t") ProjectLiftAlarm alarm, @Param("tableName") String tableName);
}
