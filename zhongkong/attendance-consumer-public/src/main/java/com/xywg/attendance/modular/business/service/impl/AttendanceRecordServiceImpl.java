package com.xywg.attendance.modular.business.service.impl;

import com.xywg.attendance.modular.business.service.AttendanceRecordService;
import com.xywg.attendance.modular.business.model.AttendanceRecord;
import com.xywg.attendance.modular.business.dao.AttendanceRecordMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
@Service
public class AttendanceRecordServiceImpl extends ServiceImpl<AttendanceRecordMapper, AttendanceRecord> implements AttendanceRecordService {

    @Override
    public void insertBatchSqlServer(List<AttendanceRecord> list) {
        baseMapper.insertBatchSqlServer(list);
    }

    @Override
    public void insertSqlServer(AttendanceRecord attendanceRecord) {
        baseMapper.insertSqlServer(attendanceRecord);
    }
}
