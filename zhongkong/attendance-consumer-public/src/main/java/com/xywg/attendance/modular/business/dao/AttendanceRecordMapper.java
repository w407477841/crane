package com.xywg.attendance.modular.business.dao;

import com.xywg.attendance.modular.business.model.AttendanceRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
public interface AttendanceRecordMapper extends BaseMapper<AttendanceRecord> {

    void insertBatchSqlServer(List<AttendanceRecord> list);

    void insertSqlServer(AttendanceRecord  attendanceRecord);
}
