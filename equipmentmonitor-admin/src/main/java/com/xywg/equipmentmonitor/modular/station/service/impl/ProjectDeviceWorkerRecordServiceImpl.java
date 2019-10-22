package com.xywg.equipmentmonitor.modular.station.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.station.dto.BindWorkerDTO;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDevice;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDeviceWorkerRecord;
import com.xywg.equipmentmonitor.modular.station.dao.ProjectDeviceWorkerRecordMapper;
import com.xywg.equipmentmonitor.modular.station.service.IProjectDeviceService;
import com.xywg.equipmentmonitor.modular.station.service.IProjectDeviceWorkerRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2019-03-20
 */
@Service
@Slf4j
public class ProjectDeviceWorkerRecordServiceImpl extends ServiceImpl<ProjectDeviceWorkerRecordMapper, ProjectDeviceWorkerRecord> implements IProjectDeviceWorkerRecordService {
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IProjectDeviceService projectDeviceService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindWorker(BindWorkerDTO bindWorkerDTO) {
           String uuid = bindWorkerDTO.getUuid();
           String deviceNo = bindWorkerDTO.getDeviceNo();
           String idCardNumber = bindWorkerDTO.getIdCardNumber();
           String name = bindWorkerDTO.getName();
           if(StrUtil.isBlank(uuid)){
               log.info("缺少参数uuid");
               return;
           }
        if(StrUtil.isBlank(deviceNo)){
            log.info("缺少参数deviceNo");
            return;
        }
        if(StrUtil.isBlank(idCardNumber)){
            log.info("缺少参数idCardNumber");
            return;
        }
        if(StrUtil.isBlank(name)){
            log.info("缺少参数name");
            return;
        }
        Wrapper<ProjectInfo>  projectInfoWrapper = new EntityWrapper<>();
        projectInfoWrapper.eq("uuid",uuid);
        ProjectInfo projectInfo = projectInfoService.selectOne(projectInfoWrapper);
        if(projectInfo==null){
            log.info("uuid：{}未匹配到项目",uuid);
            return;
        }
        Wrapper<ProjectDevice> projectDeviceWrapper = new EntityWrapper<>();
        projectDeviceWrapper.eq("device_no",deviceNo);
        projectDeviceWrapper.eq("type",3);
        projectDeviceWrapper.eq("current_flag",0);
        projectDeviceWrapper.eq("project_id",projectInfo.getId());
        ProjectDevice projectDevice =   projectDeviceService.selectOne(projectDeviceWrapper);
         if(projectDevice==null){
             log.info("deviceNo：{}未匹配到该设备",deviceNo);
             return;
         }
        /* 删除原来的绑定 */
        Wrapper<ProjectDeviceWorkerRecord> workerRecordWrapper =new EntityWrapper<>();
        workerRecordWrapper.eq("device_no",deviceNo);
        this.delete(workerRecordWrapper);
        /* 新增绑定 */
        ProjectDeviceWorkerRecord projectDeviceWorkerRecord = new ProjectDeviceWorkerRecord();
        projectDeviceWorkerRecord.setDeviceId(projectDevice.getId());
        projectDeviceWorkerRecord.setDeviceNo(deviceNo);
        projectDeviceWorkerRecord.setIdCardType(1);
        projectDeviceWorkerRecord.setIdCardNumber(idCardNumber);
        projectDeviceWorkerRecord.setName(name);
        projectDeviceWorkerRecord.setProjectId(projectInfo.getId());
        projectDeviceWorkerRecord.setOrgId(projectInfo.getOrgId());
        projectDeviceWorkerRecord.setIsDel(0);
        this.insert(projectDeviceWorkerRecord);
    }
}
