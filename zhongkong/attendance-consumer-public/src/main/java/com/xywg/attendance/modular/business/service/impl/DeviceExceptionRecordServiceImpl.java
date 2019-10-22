package com.xywg.attendance.modular.business.service.impl;

import com.xywg.attendance.modular.business.model.DeviceExceptionRecord;
import com.xywg.attendance.modular.business.dao.DeviceExceptionRecordMapper;
import com.xywg.attendance.modular.business.service.DeviceExceptionRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
@Service
public class DeviceExceptionRecordServiceImpl extends ServiceImpl<DeviceExceptionRecordMapper, DeviceExceptionRecord> implements DeviceExceptionRecordService {

    @Override
    public void insertSqlServer(DeviceExceptionRecord deviceExceptionRecord) {
        baseMapper.insertSqlServer(deviceExceptionRecord);
    }
}
