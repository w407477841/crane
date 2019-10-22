package com.xywg.attendance.modular.led.web;


import com.xywg.attendance.modular.led.model.AttendanceNumber;
import com.xywg.attendance.modular.led.model.EntriesExitsQuantityVo;
import com.xywg.attendance.modular.led.model.Parameter;
import com.xywg.attendance.modular.led.model.WorkerInfoVo;
import com.xywg.attendance.modular.led.service.LedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *  led大屏 请求数据
 * @author z
 * @since 2019-04-16
 */
@RestController
@RequestMapping("/led")
public class LedController {
    @Autowired
    private LedService ledService;

    /**
     * 获取该项目下的工人
     *
     * @param projectName
     * @return
     */
    @PostMapping("/getWorkerList")
    public List<WorkerInfoVo> getWorkerList(@RequestBody Parameter projectName) {
        return ledService.getWorkerList(projectName.getProjectName());
    }

    /**
     * 获取当日进退场人数
     *
     * @param projectName
     * @return
     */
    @PostMapping("/getEntriesExitsQuantity")
    public EntriesExitsQuantityVo getEntriesExitsQuantity(@RequestBody Parameter projectName) {
        return ledService.getEntriesExitsQuantity(projectName.getProjectName());
    }


    /**
     * 获取当日考勤人数
     *
     * @param projectName
     * @return
     */
    @PostMapping("/getAttendanceNumber")
    public AttendanceNumber getAttendanceNumber(@RequestBody Parameter projectName) {
        return ledService.getAttendanceNumber(projectName.getProjectName());
    }

}

