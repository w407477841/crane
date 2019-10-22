package com.xywg.attendance.modular.led.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.attendance.common.utils.Base64Util;
import com.xywg.attendance.modular.command.model.PerInf;
import com.xywg.attendance.modular.command.service.IAttendanceRecordService;
import com.xywg.attendance.modular.command.service.IPerInfService;
import com.xywg.attendance.modular.led.model.AttendanceNumber;
import com.xywg.attendance.modular.led.model.EntriesExitsQuantityVo;
import com.xywg.attendance.modular.led.model.WorkerInfoVo;
import com.xywg.attendance.modular.led.service.LedService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
@Service
public class LedServiceImpl implements LedService {
    @Autowired
    private IPerInfService perInfService;
    @Autowired
    private IAttendanceRecordService attendanceRecordService;


    @Override
    public List<WorkerInfoVo> getWorkerList(String projectName) {
        EntityWrapper<PerInf> ew = new EntityWrapper<>();
        ew.eq("project_name", projectName);
        List<PerInf> perInfList = perInfService.selectList(ew);

        List<WorkerInfoVo> workerInfoVoList = new ArrayList<>();
        for (PerInf per : perInfList) {
            String photoBase64 = per.getPhote() == null ? "" : Base64Util.encode(per.getPhote());
            WorkerInfoVo vo = new WorkerInfoVo(
                    per.getClassNo()== null ? "" : per.getClassNo(),
                    per.getMobile()== null ? "" : per.getMobile(),
                    photoBase64,
                    per.getProjectName()== null ? "" : per.getProjectName(),
                    per.getPerName()== null ? "" : per.getPerName(),
                    per.getIdNo()== null ? "" : per.getIdNo(),
                    per.getWorkKindName()== null ? "" : per.getWorkKindName(),
                    "", "", ""
            );
            workerInfoVoList.add(vo);
        }

        return workerInfoVoList;
    }

    @Override
    public EntriesExitsQuantityVo getEntriesExitsQuantity(String projectName) {
        EntityWrapper<PerInf> ew = new EntityWrapper<>();
        ew.eq("project_name", projectName);
        List<PerInf> perInfList = perInfService.selectList(ew);
        if (perInfList == null || perInfList.size() == 0 || StringUtils.isBlank(perInfList.get(0).getProjectCode())) {
            return new EntriesExitsQuantityVo(0, 0);
        }
        String projectCode = perInfList.get(0).getProjectCode();
        return attendanceRecordService.getEntriesExitsQuantity(projectCode);
    }


    @Override
    public AttendanceNumber getAttendanceNumber(String projectName) {
        EntityWrapper<PerInf> ew = new EntityWrapper<>();
        ew.eq("project_name", projectName);
        List<PerInf> perInfList = perInfService.selectList(ew);
        if (perInfList == null || perInfList.size() == 0 || StringUtils.isBlank(perInfList.get(0).getProjectCode())) {
            return new AttendanceNumber(0);
        }
        String projectCode = perInfList.get(0).getProjectCode();
        return attendanceRecordService.getAttendanceNumber(projectCode);
    }


}
