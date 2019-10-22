package com.xywg.equipmentmonitor.modular.device.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.dto.*;
import com.xywg.equipmentmonitor.modular.baseinfo.model.ProjectUser;
import com.xywg.equipmentmonitor.modular.device.dto.CountAlarmByDeviceNo;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectEnvironmentMessageDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitorAlarm;

import com.xywg.equipmentmonitor.modular.device.vo.AlarmInfoVO;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectMessageModel;
import com.xywg.equipmentmonitor.modular.station.vo.ProjectEnvironmentMonitorAlarmVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@Service
public interface ProjectEnvironmentMonitorAlarmService extends IService<ProjectEnvironmentMonitorAlarm> {
    /**
     * 实时监控明细一览
     * @param res
     * @return
     */
    ResultDTO<DataDTO<List<ProjectEnvironmentMonitorAlarm>>> selectPageList(RequestDTO<ProjectEnvironmentMonitorAlarm> res);

    /**
     * 按设备count实时监控各种异常次数
     * @return
     */
    ResultDTO<List<CountAlarmByDeviceNo>> countAlarmByDeviceNo(RequestDTO<CountAlarmByDeviceNo> res);

    /**
     * 按设备count实时监控各种异常次数明细
     * @return
     */
    ResultDTO<DataDTO<List<ProjectEnvironmentMonitorAlarm>>> countAlarmByDeviceNoDetail(RequestDTO<CountAlarmByDeviceNo> res);

    /**
     * 根据报警信息ID获取当前项目下的工人
     * @param monitorId
     * @return
     */
    ResultDTO<List<ProjectUser>> getWorkListByMonitorId(Integer monitorId);

    /**
     * 短信
     * @param requestDTO
     * @return
     */
    ResultDTO  handleSMSSubmit(RequestDTO<ProjectEnvironmentMessageDTO>  requestDTO);

    /**
     * 短信模板
     * @param res
     * @return
     */
    ResultDTO<List<ProjectMessageModel>> getSMSModel(RequestDTO<ProjectMessageModel> res);


    AppResultDTO<Object> updateAlarm(String tableName, Integer [] ids, Integer userId, String userName );

    /**
     * 报警信息
     * @param uuids  项目号
     * @return
     */
    AppDataResultDTO<List<AlarmInfoVO>> getAlarmInfo(String []uuids, Integer pageSize, Integer pageNum);

    /**
     * 报警数量
     * @param uuids
     * @param pageSize
     * @param pageNum
     * @return
     */
    AppDataResultDTO<Integer> getAlarmAccount(String []uuids, Integer pageSize, Integer pageNum);
    /**
     * 报警信息
     * @param uuids  项目号
     * @return
     */
    AppDataResultDTO<List<AlarmInfoVO>> getAlarmInfoByFlag(int flag,String []uuids, Integer pageSize, Integer pageNum);


    /**
     *  给智慧工地的
     * @param month
     * @param projectId
     * @return
     */
    List<ProjectEnvironmentMonitorAlarmVO> getAlarms2zhgd(String month, Integer projectId, String time);

    /**
     * 银江国际项目
     * @param month
     * @param projectId
     * @param time
     * @return
     */
    List<ProjectEnvironmentMonitorAlarmVO> getAlarms2yjgj(String month, Integer projectId, String time,String deviceNo,Integer status);

    /**
     *  智慧工地报警
     * @param month
     * @param projectId
     * @param time
     * @param deviceNo
     * @param status
     * @return
     */
    List<ProjectEnvironmentMonitorAlarmVO> getLiftAlarms2Zhgd(String month, Integer projectId, String time,String deviceNo,Integer status);

}
