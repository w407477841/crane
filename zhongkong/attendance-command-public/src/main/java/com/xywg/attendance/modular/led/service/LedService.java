package com.xywg.attendance.modular.led.service;

import com.xywg.attendance.modular.led.model.AttendanceNumber;
import com.xywg.attendance.modular.led.model.EntriesExitsQuantityVo;
import com.xywg.attendance.modular.led.model.WorkerInfoVo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
public interface LedService {

    /**
     * 根据项目名称获取工人列表
     *
     * @param projectName
     * @return
     */
    List<WorkerInfoVo> getWorkerList(@RequestBody String projectName);

    /**
     * 获取当日进出场人数
     *
     * @param projectName
     * @return
     */
    EntriesExitsQuantityVo getEntriesExitsQuantity(String projectName);

    /**
     * 获取当日考勤人数
     *
     * @param projectName
     * @return
     */
    AttendanceNumber getAttendanceNumber(String projectName);


}
