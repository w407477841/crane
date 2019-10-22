package com.xywg.equipmentmonitor.modular.device.controller;

import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectSprayDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectSprayBind;
import com.xywg.equipmentmonitor.modular.device.service.ProjectSprayBindService;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectSprayVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 20:02
 */
@RestController
@RequestMapping("/ssdevice/device/projectSprayBind")
public class ProjectSprayBindController extends BaseController<ProjectSprayBind,ProjectSprayBindService>{

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

    @PostMapping("/getList")
    @ApiOperation("查询")
    public ResultDTO<DataDTO<List<ProjectSprayVO>>> selectPageList(@RequestBody RequestDTO requestDTO) {
        return service.getList(Integer.parseInt(requestDTO.getId().toString()));
    }

    @PostMapping("/add")
    @ApiOperation("新增")
    public ResultDTO<Object> insertSpray(@RequestBody  RequestDTO<ProjectSprayDTO> requestDTO){
        boolean flag=service.add(requestDTO.getBody());
        if(flag){
            return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
        }else{
            return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
        }
    }
}
