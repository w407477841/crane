package com.xywg.equipmentmonitor.modular.device.controller;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectSprayDetail;
import com.xywg.equipmentmonitor.modular.device.service.ProjectSprayDetailService;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 10:53
 */
@RestController
@RequestMapping("/ssdevice/device/projectSprayDetail")
public class ProjectSprayDetailController extends BaseController<ProjectSprayDetail,ProjectSprayDetailService>{

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
    @PostMapping("/getBySprayId")
    public ResultDTO<DataDTO<List<ProjectSprayDetail>>> getBySprayId(@RequestBody RequestDTO request) {
        return service.getBySprayId(request);
    }
}
