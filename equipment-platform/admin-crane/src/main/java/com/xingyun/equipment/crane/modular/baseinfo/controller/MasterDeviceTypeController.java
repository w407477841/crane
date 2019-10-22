package com.xingyun.equipment.crane.modular.baseinfo.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.enums.OperationEnum;
import com.xingyun.equipment.core.enums.ResultCodeEnum;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.baseinfo.model.MasterDeviceType;
import com.xingyun.equipment.crane.modular.baseinfo.service.IMasterDeviceTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/8/20
 *TIME:20:15
 */
@RestController
@RequestMapping(value = "/admin-crane/baseInfo/masterDeviceType")
public class MasterDeviceTypeController extends BaseController<MasterDeviceType,IMasterDeviceTypeService> {
    @Override
    public String insertRole() {
        return "project:device:insert";
    }

    @Override
    public String updateRole() {
        return "project:device:update";
    }

    @Override
    public String deleteRole() {
        return "project:device:delete";
    }

    @Override
    public String viewRole() {
        return "project:device:view";
    }

    @ApiOperation("新增设备类型")
    @Override
    @PostMapping("insert")
    public ResultDTO<MasterDeviceType>  insert(@RequestBody RequestDTO<MasterDeviceType> t){
        try {
            if(service.insert(t.getBody())){
                return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
            }
        }catch (RuntimeException r) {
            r.printStackTrace();
            if("名称重复".equals(r.getMessage())) {
                return ResultDTO.factory(ResultCodeEnum.DICT_SERVICE_TYPE_NAME_REPEAT);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
    }

    @ApiOperation("修改设备类型")
    @Override
    @PostMapping("update")
    public ResultDTO<MasterDeviceType>  update(@RequestBody  RequestDTO<MasterDeviceType> t){
        try {
            if(service.updateById(t.getBody())){
                return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.factory(ResultCodeEnum.DICT_SERVICE_TYPE_NAME_REPEAT);
        }

        return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
    }

    @ApiOperation("获取设备类型不分页")
    @PostMapping(value = "/selectDeviceType")
    public ResultDTO<List<MasterDeviceType>> selectDeviceType(RequestDTO<MasterDeviceType> t) {
        try {
            return new ResultDTO<>(true,service.selectDeviceType());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取设备类型")
    @PostMapping(value ="/selectDeviceTypeInfo")
    public ResultDTO<DataDTO<List<MasterDeviceType>>> selectDeviceTypeInfo(@RequestBody RequestDTO<MasterDeviceType> requestDTO) {
        try {
            Page<MasterDeviceType> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
            List<MasterDeviceType> list = service.selectDeviceTypeInfo(page,requestDTO);
            return new ResultDTO<DataDTO<List<MasterDeviceType>>>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }
}
