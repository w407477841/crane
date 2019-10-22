package com.xingyun.equipment.crane.modular.baseinfo.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.core.enums.OperationEnum;
import com.xingyun.equipment.core.enums.PermissionEnum;
import com.xingyun.equipment.core.enums.ResultCodeEnum;
import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.baseinfo.model.ProjectUser;
import com.xingyun.equipment.crane.modular.baseinfo.service.IProjectUserService;
import com.xingyun.equipment.crane.modular.baseinfo.vo.ProjectUserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/8/20
 *TIME:8:49
 */
@RestController
@RequestMapping(value = "/admin-crane/baseInfo/projectUser")
public class ProjectUserController extends BaseController<ProjectUser,IProjectUserService> {
    @Override
    public String insertRole() {
        return "project:user:insert";
    }

    @Override
    public String updateRole() {
        return "project:user:update";
    }

    @Override
    public String deleteRole() {
        return "project:user:delete";
    }

    @Override
    public String viewRole() {
        return "project:user:view";
    }

    @ApiOperation("获取人员信息不分页")
    @PostMapping(value = "/selectUser")
    public ResultDTO<List<ProjectUser>> selectUser(RequestDTO<ProjectUser> t) {
        try {
            return new ResultDTO<>(true,service.selectUser(t));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取人员信息")
    @PostMapping(value = "/selectUserInfo")
    public ResultDTO<DataDTO<List<ProjectUserVo>>> selectUserInfo(@RequestBody RequestDTO<ProjectUserVo> requestDTO) {
        try {
            Page<ProjectUserVo> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
            List<ProjectUserVo> list = service.selectUserInfo(page,requestDTO);
            return new ResultDTO<DataDTO<List<ProjectUserVo>>>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("新增人员")
    @Override
    public ResultDTO<ProjectUser> insert(@RequestBody RequestDTO<ProjectUser> t) {
        try {
            if(service.insert(t.getBody())) {
                return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
            }
        }catch (Exception e) {
            e.printStackTrace();
            if("身份证号重复".equals(e.getMessage())) {
                return ResultDTO.factory(ResultCodeEnum.USER_IDENTITY_CODE_REPEAT);
            }
        }
        return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
    }

    @ApiOperation("修改人员")
    @Override
    public ResultDTO<ProjectUser> update(@RequestBody RequestDTO<ProjectUser> t) {
        try {
            if(service.updateById(t.getBody())) {
                return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
            }
        }catch (RuntimeException re) {
            re.printStackTrace();
            return ResultDTO.factory(ResultCodeEnum.USER_IDENTITY_CODE_REPEAT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
    }
}
