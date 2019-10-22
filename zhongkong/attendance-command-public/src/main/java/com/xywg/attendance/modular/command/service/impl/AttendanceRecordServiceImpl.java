package com.xywg.attendance.modular.command.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.attendance.modular.command.dao.AttendanceRecordMapper;
import com.xywg.attendance.modular.command.model.AttendanceRecord;
import com.xywg.attendance.modular.command.service.IAttendanceRecordService;
import com.xywg.attendance.modular.led.model.AttendanceNumber;
import com.xywg.attendance.modular.led.model.EntriesExitsQuantityVo;
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
public class AttendanceRecordServiceImpl extends ServiceImpl<AttendanceRecordMapper, AttendanceRecord> implements IAttendanceRecordService {

    @Override
    public EntriesExitsQuantityVo getEntriesExitsQuantity(String projectCode) {
        return baseMapper.getEntriesExitsQuantity(projectCode);
    }

    @Override
    public AttendanceNumber getAttendanceNumber(String projectCode) {
        return baseMapper.getAttendanceNumber(projectCode);
    }
}
