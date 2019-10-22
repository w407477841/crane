package com.xywg.attendance.modular.command.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.attendance.modular.command.model.AttendanceRecord;
import com.xywg.attendance.modular.led.model.AttendanceNumber;
import com.xywg.attendance.modular.led.model.EntriesExitsQuantityVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
public interface AttendanceRecordMapper extends BaseMapper<AttendanceRecord> {

    EntriesExitsQuantityVo getEntriesExitsQuantity(String projectCode);

    AttendanceNumber getAttendanceNumber(String projectCode);
}
