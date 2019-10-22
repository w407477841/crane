package com.xywg.iot.modules.helmet.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.iot.modules.helmet.model.ProjectHelmetAlarm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 */
public interface ProjectHelmetAlarmMapper extends BaseMapper<ProjectHelmetAlarm> {

    void insertBatchByMonth(@Param("tableName") String tableName,@Param("list") List<ProjectHelmetAlarm> list);
}
