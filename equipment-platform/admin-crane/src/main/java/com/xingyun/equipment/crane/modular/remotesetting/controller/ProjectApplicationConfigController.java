package com.xingyun.equipment.crane.modular.remotesetting.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.crane.core.aop.ZbusProducerHolder;
import com.xingyun.equipment.core.enums.OperationEnum;
import com.xingyun.equipment.core.enums.ResultCodeEnum;
import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectApplicationConfig;
import com.xingyun.equipment.crane.modular.remotesetting.service.IProjectApplicationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2019/1/18
 *TIME:10:13
 */
@RestController
@RequestMapping("/admin-crane/remoteSetting/projectApplicationConfig")
public class ProjectApplicationConfigController extends BaseController<ProjectApplicationConfig,IProjectApplicationConfigService> {
    @Autowired
    private ZbusProducerHolder zbusProducerHolder;
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

    @Override
    public ResultDTO<ProjectApplicationConfig> insert(@RequestBody RequestDTO<ProjectApplicationConfig> requestDTO) {
        try {
            Wrapper<ProjectApplicationConfig> wrapper = new EntityWrapper<>();
            wrapper.eq("name",requestDTO.getBody().getName());
            List<ProjectApplicationConfig> list = service.selectList(wrapper);
            if(list.size() > 0) {
                return ResultDTO.factory(ResultCodeEnum.APP_NAME_REPEAT);
            }
            ProjectApplicationConfig projectApplicationConfig = requestDTO.getBody();
            projectApplicationConfig.setTopic(RandomUtil.randomString(16));
            if(service.insert(projectApplicationConfig)) {
                zbusProducerHolder.addTopic(projectApplicationConfig.getTopic());
                return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
    }

    @Override
    public ResultDTO<ProjectApplicationConfig> update(@RequestBody RequestDTO<ProjectApplicationConfig> requestDTO) {
        try {
            Wrapper<ProjectApplicationConfig> wrapper = new EntityWrapper<>();
            wrapper.eq("name",requestDTO.getBody().getName()).ne("id",requestDTO.getBody().getId());
            List<ProjectApplicationConfig> list = service.selectList(wrapper);
            if(list.size() > 0) {
                return ResultDTO.factory(ResultCodeEnum.APP_NAME_REPEAT);
            }
            if(service.updateById(requestDTO.getBody())) {
                return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
    }

    @Override
    public ResultDTO<ProjectApplicationConfig> deletes(@RequestBody RequestDTO<ProjectApplicationConfig> requestDTO) {
        try {
            Wrapper<ProjectApplicationConfig> wrapper = new EntityWrapper<>();
            wrapper.in("id",requestDTO.getIds());
            List<ProjectApplicationConfig> list = service.selectList(wrapper);
            List<String> topics = new ArrayList<>(10);
            for(int i = 0;i < list.size();i++) {
                topics.add(list.get(i).getTopic());
            }
            if(service.deleteBatchIds(requestDTO.getIds())) {
                for(int i = 0;i < topics.size();i++) {
                    zbusProducerHolder.removeTopic(topics.get(i));
                }
                return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.resultFactory(OperationEnum.DELETE_ERROR);
    }

    @Override
    public ResultDTO<DataDTO<List<ProjectApplicationConfig>>> selectList(@RequestBody RequestDTO<ProjectApplicationConfig> requestDTO) {
        Page<ProjectApplicationConfig> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
        Wrapper<ProjectApplicationConfig> wrapper = new EntityWrapper<>();
        wrapper.orderBy("create_time",false);
        if(requestDTO.getKey() != null && !"".equals(requestDTO.getKey())) {
            wrapper.like("name",requestDTO.getKey());
        }
        Page<ProjectApplicationConfig> list = service.selectPage(page,wrapper);
        return new ResultDTO<>(true,DataDTO.factory(list.getRecords(),page.getTotal()));
    }
}
