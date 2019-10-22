package com.xingyun.equipment.admin.modular.infromation.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.common.constant.ResultCodeEnum;
import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectWaterMeter;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectTargetSetWater;
import com.xingyun.equipment.admin.modular.infromation.service.IProjectTargetSetWaterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/10/15
 *TIME:11:13
 */
@RestController
@RequestMapping("/ssdevice/information/projectTargetSetWater")
public class ProjectTargetSetWaterController extends BaseController<ProjectTargetSetWater,IProjectTargetSetWaterService> {
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

    @PostMapping("/selectTargetSetWater")
    @ApiOperation("获取列表")
    public ResultDTO<DataDTO<List<ProjectTargetSetWater>>> selectTargetSetWater(@RequestBody RequestDTO<ProjectTargetSetWater> requestDTO) {
        try {
            Page<ProjectTargetSetWater> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
            List<ProjectTargetSetWater> list = service.selectTargetSetWater(page,requestDTO);
            return new ResultDTO<>(true,DataDTO.factory(list,page.getTotal()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO<>(false);
    }

    @Override
    public ResultDTO<ProjectTargetSetWater> insert(@RequestBody RequestDTO<ProjectTargetSetWater> requestDTO) {
        Wrapper<ProjectTargetSetWater> wrapper = new EntityWrapper<>();
        wrapper.eq("specification",requestDTO.getBody().getSpecification());
        wrapper.eq("manufactor",requestDTO.getBody().getManufactor());
        if(service.selectList(wrapper).size() > 0) {
            return ResultDTO.factory(ResultCodeEnum.STANDARD_REPEAT);
        }
        return super.insert(requestDTO);
    }

    @Override
    public ResultDTO<ProjectTargetSetWater> update(@RequestBody RequestDTO<ProjectTargetSetWater> requestDTO) {
        Wrapper<ProjectTargetSetWater> wrapper = new EntityWrapper<>();
        wrapper.eq("specification",requestDTO.getBody().getSpecification());
        wrapper.eq("manufactor",requestDTO.getBody().getManufactor());
        wrapper.ne("id",requestDTO.getBody().getId());
        if(service.selectList(wrapper).size() > 0) {
            return ResultDTO.factory(ResultCodeEnum.STANDARD_REPEAT);
        }
        return super.update(requestDTO);
    }

    @Override
    public ResultDTO<List<ProjectTargetSetWater>> selectListNoPage(RequestDTO<ProjectTargetSetWater> requestDTO) {
        Wrapper<ProjectTargetSetWater> wrapper = new EntityWrapper<>();
        wrapper.in("org_id",requestDTO.getOrgIds());
        return new ResultDTO<>(true,service.selectList(wrapper));
    }
}
