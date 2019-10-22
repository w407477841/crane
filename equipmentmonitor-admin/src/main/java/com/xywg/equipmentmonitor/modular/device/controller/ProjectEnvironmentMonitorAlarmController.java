package com.xywg.equipmentmonitor.modular.device.controller;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.baseinfo.model.ProjectUser;
import com.xywg.equipmentmonitor.modular.device.dto.CountAlarmByDeviceNo;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectEnvironmentMessageDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitorAlarm;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorAlarmService;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectMessageModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company:星云网格
 *
 * @author zhouyujie
 * @date 2018年8月21日
 */
@RestController
@RequestMapping("ssdevice/project/projectEnvironmentMonitorAlarm")
public class ProjectEnvironmentMonitorAlarmController extends BaseController<ProjectEnvironmentMonitorAlarm, ProjectEnvironmentMonitorAlarmService> {

    @Override
    public String insertRole() {
        return null;
    }

    @Override
    public String updateRole() {
        return null;
    }

    @Override
    public String deleteRole() {
        return null;
    }

    @Override
    public String viewRole() {
        return null;
    }

    /**
     * 实时监控一览
     *
     * @param res
     * @return
     */
    @ApiOperation("实时监控明细一览")
    @PostMapping("selectPageList")
    ResultDTO<DataDTO<List<ProjectEnvironmentMonitorAlarm>>> selectPageList(@RequestBody RequestDTO<ProjectEnvironmentMonitorAlarm> res) {
        return service.selectPageList(res);
    }

    /**
     * 实时监控各种异常count次数
     *
     * @param res
     * @return
     */
    @ApiOperation("按设备count实时监控各种异常次数")
    @PostMapping("countAlarmByDeviceNo")
    ResultDTO<List<CountAlarmByDeviceNo>> countAlarmByDeviceNo(@RequestBody RequestDTO<CountAlarmByDeviceNo> res) {

        return service.countAlarmByDeviceNo(res);
    }


    /**
     * 实时监控各种异常明细
     *
     * @param res
     * @return
     */
    @ApiOperation("按设备count实时监控各种异常次数明细")
    @PostMapping("countAlarmByDeviceNoDetail")
    ResultDTO<DataDTO<List<ProjectEnvironmentMonitorAlarm>>> countAlarmByDeviceNoDetail(@RequestBody RequestDTO<CountAlarmByDeviceNo> res) {
        return service.countAlarmByDeviceNoDetail(res);
    }

    /**
     * 根据报警信息ID获取当前项目下的工人
     *
     * @param requestDTO
     * @return
     */
    @ApiOperation("根据报警信息ID获取当前项目下的工人")
    @PostMapping("getWorkListByMonitorId")
    ResultDTO<List<ProjectUser>>  getWorkListByMonitorId(@RequestBody RequestDTO  requestDTO) {
        return service.getWorkListByMonitorId(requestDTO.getMonitorId());
    }




    /**
     * 报警(扬尘)短信
     * @param requestDTO
     * @return
     */
    @ApiOperation("报警(扬尘)短信")
    @PostMapping("handleSMSSubmit")
    ResultDTO  handleSMSSubmit(@RequestBody RequestDTO<ProjectEnvironmentMessageDTO>  requestDTO) {

        return service.handleSMSSubmit(requestDTO);
    }


    @ApiOperation("获取短信模板(不分页)")
    @PostMapping("getSMSModel")
    ResultDTO<List<ProjectMessageModel>> getSMSModel(@RequestBody RequestDTO<ProjectMessageModel> res){

        return service.getSMSModel(res);
    }
}
