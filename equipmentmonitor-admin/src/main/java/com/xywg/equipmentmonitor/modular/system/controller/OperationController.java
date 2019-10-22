package com.xywg.equipmentmonitor.modular.system.controller;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.system.model.Operation;
import com.xywg.equipmentmonitor.modular.system.service.IOperationService;
import com.xywg.equipmentmonitor.modular.system.vo.OperationVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/9/6
 *TIME:13:58
 */
@RestController
@RequestMapping("/ssdevice/system/operation")
public class OperationController extends BaseController<Operation,IOperationService> {
    @Override
    public String insertRole() {
        return "system:operation:insert";
    }

    @Override
    public String updateRole() {
        return "system:operation:update";
    }

    @Override
    public String deleteRole() {
        return "system:operation:delete";
    }

    @Override
    public String viewRole() {
        return "system:operation:view";
    }

    @ApiOperation("查询权限树")
    @PostMapping("/selectTreeOperation")
    public ResultDTO<List<OperationVo>> selectTreeOperation(@RequestBody RequestDTO<OperationVo> requestDTO) {
        try {
            return new ResultDTO<>(true,service.selectTreeOperation(requestDTO));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    


    /**
    * @author: wangyifei
    * Description: 获取当前用户的对应的当前权限
    * Date: 10:34 2018/9/7
    */
    @ApiOperation("查询左侧菜单")
    @GetMapping("getPermissions")
    public Object getLiftMenu(){

        return new ResultDTO(true, service.getPermissions());

    }

}
