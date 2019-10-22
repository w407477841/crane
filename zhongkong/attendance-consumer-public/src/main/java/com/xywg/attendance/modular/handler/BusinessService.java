package com.xywg.attendance.modular.handler;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.attendance.modular.business.model.AmInf;
import com.xywg.attendance.modular.business.model.AttendanceRecord;
import com.xywg.attendance.modular.business.model.DeviceExceptionRecord;
import com.xywg.attendance.modular.business.model.PerInf;
import com.xywg.attendance.modular.business.service.AmInfService;
import com.xywg.attendance.modular.business.service.AttendanceRecordService;
import com.xywg.attendance.modular.business.service.DeviceExceptionRecordService;
import com.xywg.attendance.modular.business.service.PerInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * @author hjy
 * @date 2019/4/16
 */
@Service
public class BusinessService {
    @Autowired
    private AttendanceRecordService attendanceRecordService;
    @Autowired
    private AmInfService amInfService;
    @Autowired
    private PerInfService perInfService;
    @Autowired
    private DeviceExceptionRecordService exceptionRecordService;

    /**
     * @param idNo
     * @param attendanceTime
     * @param sn
     * @param base64Photo    考勤照片base64
     */
    public void handleAttendance(String idNo, Date attendanceTime, String sn, String base64Photo) {
        Wrapper<AttendanceRecord> perWp = new EntityWrapper<>();
        perWp.eq("ID_NO", idNo);
        perWp.eq("Record", attendanceTime);
        AttendanceRecord attendanceRecordDb = attendanceRecordService.selectOne(perWp);
        // 无论是存在正常考勤数据存在 还是异常考勤数据存在 直接更新图片字段即可,然后结束此次操作
        //有可能普通普通正常考勤数据先入库
        if (attendanceRecordDb != null) {
            attendanceRecordDb.setPhotoUrl(base64Photo);
            attendanceRecordService.updateById(attendanceRecordDb);
            return;
        }

        Wrapper<DeviceExceptionRecord> perER = new EntityWrapper<>();
        perER.eq("id_card_number", idNo);
        perER.eq("time", attendanceTime);
        //查询异常考勤数据里是否存在
        DeviceExceptionRecord deviceExceptionRecord = exceptionRecordService.selectOne(perER);
        if (deviceExceptionRecord != null) {
            deviceExceptionRecord.setPhoto(base64Photo);
            exceptionRecordService.updateById(deviceExceptionRecord);
            return;
        }


        Wrapper<PerInf> perInfEw = new EntityWrapper<>();
        perInfEw.eq("Id_No", idNo);
        //查询人员信息
        PerInf perInf = perInfService.selectOne(perInfEw);
        //查询设备信息
        Wrapper<AmInf> amInfEw = new EntityWrapper<>();
        amInfEw.eq("AM_Code", sn);
        List<AmInf> amInfList = amInfService.selectList(amInfEw);
        Integer atType = null;
        if (amInfList.size() > 0) {
            atType = amInfList.get(0).getAtType();
        }
        if (perInf == null) {
            DeviceExceptionRecord record = new DeviceExceptionRecord(
                    sn, 1, idNo, attendanceTime,
                    base64Photo, new Date(), 2, 0, atType);
            //存储异常考勤
            exceptionRecordService.insertSqlServer(record);
            return;
        }


        Wrapper<AmInf> amInfEwNew = new EntityWrapper<>();
        amInfEwNew.eq("AM_Code", sn);
        amInfEwNew.eq("Project_Code", perInf.getProjectCode());
        //查询设备信息
        AmInf amInf = amInfService.selectOne(amInfEwNew);
        if (amInf == null) {
            DeviceExceptionRecord record = new DeviceExceptionRecord(
                    sn, 1, idNo, attendanceTime,
                    base64Photo, new Date(), 1, 0, atType);
            //存储异常考勤
            exceptionRecordService.insertSqlServer(record);
            return;
        }

        //正常考勤
        AttendanceRecord attendanceRecord = new AttendanceRecord(sn, amInf.getProjectCode(), attendanceTime, amInf.getAtType(), idNo, base64Photo);
        attendanceRecordService.insertSqlServer(attendanceRecord);
    }

}
