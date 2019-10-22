package com.xywg.equipment.monitor.iot.modular.romote.dao;

import com.xywg.equipment.monitor.iot.modular.romote.model.ProjectMessageUserDeviceErrorLog;
import org.apache.ibatis.annotations.Param;

/**

 */
public interface ProjectMessageUserDeviceErrorLogMapper {

    /**
     * 查询一条
     * @param deviceType
     * @return
     */
    ProjectMessageUserDeviceErrorLog select(@Param("deviceType") Integer deviceType);
  /*  *//**
     * 插入
     * @param projectDeviceErrorLog
     *//*
    void insert(ProjectDeviceErrorLog projectDeviceErrorLog);*/

}
