package com.xywg.attendance.modular.command.service;

import com.xywg.attendance.modular.command.model.AttendanceRecord;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.attendance.modular.led.model.AttendanceNumber;
import com.xywg.attendance.modular.led.model.EntriesExitsQuantityVo;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
public interface IAttendanceRecordService extends IService<AttendanceRecord> {

    /**
     * 统计当日进退场人数
     * @param projectCode
     * @return
     */
    EntriesExitsQuantityVo getEntriesExitsQuantity (String projectCode);

    /**
     * 获取当日项目下考勤人数
     * @param projectCode
     * @return
     */
    AttendanceNumber getAttendanceNumber(String projectCode);


}
