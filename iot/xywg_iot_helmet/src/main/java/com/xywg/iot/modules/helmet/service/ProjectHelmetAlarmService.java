package com.xywg.iot.modules.helmet.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.modules.helmet.model.ProjectHelmetAlarm;

import java.util.List;

/**
 *  服务类
 *
 * @author hjy
 */
public interface ProjectHelmetAlarmService extends IService<ProjectHelmetAlarm> {

  void  insertBatchByMonth(String tableName, List<ProjectHelmetAlarm> list);
}
