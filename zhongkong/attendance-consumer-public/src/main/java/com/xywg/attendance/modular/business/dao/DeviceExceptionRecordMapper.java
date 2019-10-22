package com.xywg.attendance.modular.business.dao;

import com.xywg.attendance.modular.business.model.DeviceExceptionRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
public interface DeviceExceptionRecordMapper extends BaseMapper<DeviceExceptionRecord> {

    void insertSqlServer(DeviceExceptionRecord deviceExceptionRecord);
}
