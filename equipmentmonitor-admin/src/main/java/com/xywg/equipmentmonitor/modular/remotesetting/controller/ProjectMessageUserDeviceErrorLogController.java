package com.xywg.equipmentmonitor.modular.remotesetting.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectMessageUserDeviceErrorLog;
import com.xywg.equipmentmonitor.modular.remotesetting.service.ProjectMessageUserDeviceErrorLogService;
import com.xywg.equipmentmonitor.modular.system.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 日志用户
 * @author hjy
 */
@RestController
@RequestMapping("/ssdevice/remoteSetting/logUser")
public class ProjectMessageUserDeviceErrorLogController extends BaseController<ProjectMessageUserDeviceErrorLog,ProjectMessageUserDeviceErrorLogService> {
    @Autowired
    private IUserService userService;
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

    @ApiOperation("条件分页查询")
    @PostMapping("/getPageList")
    public ResultDTO<DataDTO<List<ProjectMessageUserDeviceErrorLog>>> getPageList(@RequestBody RequestDTO request){
        return service.getPageList(request);
    }

    @ApiOperation("新增")
    @PostMapping("/insertModel")
    public ResultDTO<Object> insertModel(@RequestBody RequestDTO<ProjectMessageUserDeviceErrorLog> request){
        try {
            ProjectMessageUserDeviceErrorLog userDeviceErrorLog=request.getBody();
            Wrapper<ProjectMessageUserDeviceErrorLog> wrapper = new EntityWrapper<>();
            wrapper.eq("device_type",userDeviceErrorLog.getDeviceType());
            List<ProjectMessageUserDeviceErrorLog>   list= service.selectList(wrapper);
            if(list.size()>0){
                return new ResultDTO<>(false,null,"该设备类型已配置");
            }
            userDeviceErrorLog.setUserIds(StringUtils.join(userDeviceErrorLog.getUserIdList(),","));
            service.insert(userDeviceErrorLog);
            return new ResultDTO<>(true);
        } catch (Exception e) {
            return new ResultDTO<>(false);
        }
    }

    @ApiOperation("获取单条")
    @PostMapping("getInfoById")
    public ResultDTO<ProjectMessageUserDeviceErrorLog> getInfoById(@RequestBody RequestDTO<ProjectMessageUserDeviceErrorLog> t){
        ProjectMessageUserDeviceErrorLog  result =  service.selectById(t.getId());
        String[] userList= result.getUserIds().split(",");
        result.setUserIdList(Arrays.asList(userList));
        //result.setUserList(userService.getListUserByIds(userList));
        return new ResultDTO<>(true,result);
    }

    @Override
    @ApiOperation("编辑")
    @PostMapping("/update")
    public ResultDTO<ProjectMessageUserDeviceErrorLog> update(@RequestBody RequestDTO<ProjectMessageUserDeviceErrorLog> request){
        try {
            ProjectMessageUserDeviceErrorLog userDeviceErrorLog=request.getBody();
            userDeviceErrorLog.setUserIds(StringUtils.join(userDeviceErrorLog.getUserIdList(),","));
            if(service.updateById(userDeviceErrorLog)){
                return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
        }
        return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
    }
}

