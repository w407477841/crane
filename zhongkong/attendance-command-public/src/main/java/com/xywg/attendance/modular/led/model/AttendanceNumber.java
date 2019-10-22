package com.xywg.attendance.modular.led.model;

import lombok.Data;

/**
 * @author hjy
 * @date 2019/5/16
 */
@Data
public class AttendanceNumber {
    /**
     * 考勤人数
     */
    private Integer attcount;

    public AttendanceNumber(Integer attcount) {
        this.attcount = attcount;
    }

    public AttendanceNumber() {
    }
}
