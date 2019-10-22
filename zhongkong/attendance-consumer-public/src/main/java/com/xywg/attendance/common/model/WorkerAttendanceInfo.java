package com.xywg.attendance.common.model;

import lombok.Data;

/**
 * @author hjy
 * @date 2019/3/26
 * 第三方考勤数据实体
 */
@Data
public class WorkerAttendanceInfo {
    private String clockinCode;
    /**
     * 项目数据指纹
     */
    private String dataNumber;
    /**
     * 现场考勤系统提供商登记编号
     */
    private String bsgsBH;
    /**
     * 证件号码
     */
    private String idCardNumber;
    /**
     * 考勤时间,格式为 yyyy-MM-dd HH:mm:ss
     */
    private String time;
    /**
     * 进出标识1表示进门2表示出门
     */
    private String clockInFlag;

}
