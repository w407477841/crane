package com.xywg.equipment.monitor.modular.whf.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitorAlarm;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-21
 */
public interface IProjectEnvironmentMonitorAlarmService extends IService<ProjectEnvironmentMonitorAlarm> {


    void createAlarm( ProjectEnvironmentMonitorAlarm alarm,String tableName)throws  Exception;

}
