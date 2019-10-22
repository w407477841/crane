package com.xywg.equipmentmonitor.modular.projectmanagement.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum;
import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.StringCompress;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *@author:jixiaojun
 *DATE:2018/8/20
 *TIME:8:52
 */
@RestController
@RequestMapping(value = "/ssdevice/projectManagement/projectInfo")
public class ProjectInfoController extends BaseController<ProjectInfo,IProjectInfoService> {
    @Override
    public String insertRole() {
        return "project:info:insert";
    }

    @Override
    public String updateRole() {
        return "project:info:update";
    }

    @Override
    public String deleteRole() {
        return "project:info:delete";
    }

    @Override
    public String viewRole() {
        return "project:info:view";
    }

    @ApiOperation("新增工程")
    @PostMapping("insert")
    @Override
    public ResultDTO<ProjectInfo>  insert(@RequestBody RequestDTO<ProjectInfo> t){
        try {
            if( service.insert(t.getBody())){
                return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if("名称重复".equals(e.getMessage())) {
                return ResultDTO.factory(ResultCodeEnum.PROJECT_NAME_REPEAT);
            }
        }
        return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
    }

    @ApiOperation("修改工程")
    @PostMapping("update")
    @Override
    public ResultDTO<ProjectInfo>  update(@RequestBody RequestDTO<ProjectInfo> t){
        try {
            if(service.updateById(t.getBody())){
                return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.factory(ResultCodeEnum.PROJECT_NAME_REPEAT);
        }

        return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
    }

    @ApiOperation("获取工程label，value")
    @PostMapping(value = "/selectProjectInfo")
    public ResultDTO<List<ProjectInfoVo>> selectProjectInfo(@RequestBody RequestDTO<ProjectInfoVo> requestDTO) {
        try {
            return new ResultDTO<>(true,service.getProjectInfo(requestDTO));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    /*@ApiOperation("获取工程不分页")
    @PostMapping(value = "/selectProject")
    public ResultDTO<List<ProjectInfo>> selectProject(@RequestBody RequestDTO<ProjectInfo> requestDTO) {
        try {
            return new ResultDTO<>(true,service.selectProject(requestDTO));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }*/

    @ApiOperation("绑定智慧工地")
    @PostMapping(value = "/bindingProject")
    public ResultDTO<ProjectInfoVo> bindingProject(@RequestBody RequestDTO<ProjectInfoVo> requestDTO) {
        try {
            if(service.bindingProject(requestDTO)) {
                return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
    }

    @ApiOperation("获取智慧工地项目")
    @PostMapping(value = "/getSmartProject")
    public ResultDTO<String> getSmartProject(@RequestBody RequestDTO<ProjectInfo> requestDTO) {
        try {
            return new ResultDTO<>(true,service.getSmartProject(requestDTO));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取项目")
    @PostMapping(value = "/selectProjectList")
    public ResultDTO<DataDTO<List<ProjectInfoVo>>> selectProjectList(@RequestBody RequestDTO<ProjectInfoVo> requestDTO) {
        try {
            Page<ProjectInfoVo> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
            List<ProjectInfoVo> list = service.selectProjectInfo(page,requestDTO);
            return new ResultDTO<DataDTO<List<ProjectInfoVo>>>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("根据id获取项目")
    @PostMapping("/selectProjectById")
    public ResultDTO<ProjectInfoVo> selectProjectById(@RequestBody RequestDTO<ProjectInfoVo> requestDTO) {
        try {
            return new ResultDTO<>(true,service.selectProjectById(requestDTO));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取部门下工地数")
    @GetMapping("/getCountByOrgId")
    public ResultDTO<Map<String,Object>> getCountByOrgId(@RequestParam Integer orgId) {
        try {
            Map<String,Object> map = new HashMap<>(10);
            map.put("amount",service.getProjectCount());
            return new ResultDTO<>(true,map);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取超标工地数量")
    @GetMapping(value = "/getExceedCountByOrgId")
    public ResultDTO<Map<String,Object>> getExceedCountByOrgId(@RequestParam Integer orgId) {
        try {
            Map<String,Object> map = new HashMap<>(10);
            map.put("amount",service.selectExceedCountByOrgId(orgId));
            return new ResultDTO<>(true,map);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("获取正常工地数量")
    @GetMapping(value = "/getNormalCountByOrgId")
    public ResultDTO<Map<String,Object>> getNormalCountByOrgId(@RequestParam Integer orgId) {
        try {
            Map<String,Object> map = new HashMap<>(10);
            Wrapper<ProjectInfo> wrapper = new EntityWrapper<>();
            wrapper.eq("org_id",orgId);
            int amount = service.getProjectCount() - service.selectExceedCountByOrgId(orgId);
            map.put("amount",amount);
            return new ResultDTO<>(true,map);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @Override
    public ResultDTO<List<ProjectInfo>> selectListNoPage(@RequestBody RequestDTO<ProjectInfo> requestDTO) {
        try {
            return new ResultDTO<>(true,service.selectProject(requestDTO));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @ApiOperation("审批")
    @PostMapping("/approve")
    public ResultDTO<ProjectInfo> approve(@RequestBody RequestDTO<ProjectInfo> requestDTO) {
        try {
            List<ProjectInfo> list = new ArrayList<>(10);
            for(int i = 0;i < requestDTO.getIds().size();i++) {
                ProjectInfo projectInfo = new ProjectInfo();
                projectInfo.setId(Integer.parseInt(requestDTO.getIds().get(i).toString()));
                projectInfo.setStatus(1);
                list.add(projectInfo);
            }
            if(service.updateBatchById(list)) {
                return ResultDTO.resultFactory(OperationEnum.APPROVE_SUCCESS);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.resultFactory(OperationEnum.APPROVE_ERROR);
    }

    @ApiOperation("智慧工地新增项目")
    @PostMapping("/insertProject")
    public ResultDTO insertProject(@RequestBody ProjectInfo projectInfo) {

        try {
            if(service.insertProject(projectInfo)) {
                return  ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
            }
        }catch (Exception e) {
            e.printStackTrace();
            if(ResultCodeEnum.PROJECT_UUID_EXIST.msg().equals(e.getMessage())) {
                return  ResultDTO.factory(ResultCodeEnum.PROJECT_UUID_EXIST);
            }if(ResultCodeEnum.PROJECT_TOPIC_NOT_EXIST.msg().equals(e.getMessage())) {
                return  ResultDTO.factory(ResultCodeEnum.PROJECT_TOPIC_NOT_EXIST);
            }
        }
        return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
    }

    @ApiOperation("修改组织")
    @PostMapping("/changeOrg")
    public ResultDTO<ProjectInfo> changeOrg(@RequestBody RequestDTO<ProjectInfo> requestDTO) {
        try {
            ProjectInfo projectInfo = requestDTO.getBody();
            List<ProjectInfo> list = new ArrayList<>();
            for(int i = 0;i < requestDTO.getIds().size();i++) {
                ProjectInfo project = new ProjectInfo();
                project.setId(Integer.parseInt(requestDTO.getIds().get(i).toString()));
                project.setOrgId(projectInfo.getOrgId());
                list.add(project);
            }
            if(service.updateBatchById(list)) {
                return ResultDTO.resultFactory(OperationEnum.CHANGE_SUCCESS);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.resultFactory(OperationEnum.CHANGE_FAIL);
    }
}
