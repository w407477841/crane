package com.xywg.attendance.modular.business.service;

import com.xywg.attendance.modular.business.model.AttendanceRecord;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
public interface AttendanceRecordService extends IService<AttendanceRecord> {

    void insertBatchSqlServer(List<AttendanceRecord> list);

    void insertSqlServer(AttendanceRecord attendanceRecord);
}
