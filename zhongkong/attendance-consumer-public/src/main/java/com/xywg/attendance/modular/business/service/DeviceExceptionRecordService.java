package com.xywg.attendance.modular.business.service;

import com.xywg.attendance.modular.business.model.DeviceExceptionRecord;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
public interface DeviceExceptionRecordService extends IService<DeviceExceptionRecord> {

    void insertSqlServer(DeviceExceptionRecord deviceExceptionRecord);
}
