package com.xywg.equipmentmonitor.modular.infromation.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.infromation.dao.ProjectEnvironmentMonitorDataModelMapper;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectEnvironmentMonitorDataModel;
import com.xywg.equipmentmonitor.modular.infromation.service.IProjectEnvironmentMonitorDataModelService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-08-24
 */
@Service
public class ProjectEnvironmentMonitorDataModelServiceImpl extends ServiceImpl<ProjectEnvironmentMonitorDataModelMapper, ProjectEnvironmentMonitorDataModel> implements IProjectEnvironmentMonitorDataModelService {

    @Override
    public ResultDTO<Object> insertModel(RequestDTO<ProjectEnvironmentMonitorDataModel> request) {
        ProjectEnvironmentMonitorDataModel body = request.getBody();
        EntityWrapper<ProjectEnvironmentMonitorDataModel> ew = new EntityWrapper<ProjectEnvironmentMonitorDataModel>();
        ew.eq("is_del", 0);
        ew.eq("specification", body.getSpecification());
        ew.eq("manufactor", body.getManufactor());
        ProjectEnvironmentMonitorDataModel t = new ProjectEnvironmentMonitorDataModel();
        try{

            t = baseMapper.selectByName(ew);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (t == null) {
            body.setIsDel(0);
            baseMapper.insert(body);
            return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
        } else {
            return new ResultDTO<>(false, null, "该厂商规格型号模板已存在！");
        }
    }

    @Override
    public ResultDTO<Object> updateModel(RequestDTO<ProjectEnvironmentMonitorDataModel> request) {
        ProjectEnvironmentMonitorDataModel body = request.getBody();
        EntityWrapper<ProjectEnvironmentMonitorDataModel> ew = new EntityWrapper<ProjectEnvironmentMonitorDataModel>();
        ew.eq("is_del", 0);
        ew.eq("specification", body.getSpecification());
        ew.eq("manufactor", body.getManufactor());
        ProjectEnvironmentMonitorDataModel t = baseMapper.selectByName(ew);

        EntityWrapper<ProjectEnvironmentMonitorDataModel> ew2 = new EntityWrapper<ProjectEnvironmentMonitorDataModel>();
        ew2.eq("is_del", 0);
        ew2.eq("id", body.getId());
        ProjectEnvironmentMonitorDataModel t2 = baseMapper.selectByName(ew2);

        if (t != null && (!t2.getSpecification().equals(body.getSpecification()) || !t2.getManufactor().equals(body.getManufactor()))) {
            return new ResultDTO<>(false,null,"该厂商规格型号模板已存在！");

        } else {
            EntityWrapper<ProjectEnvironmentMonitorDataModel> ew3 = new EntityWrapper<ProjectEnvironmentMonitorDataModel>();
            ew3.eq("id", body.getId());
            baseMapper.update(body, ew3);
            return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
        }
    }

    @Override
    public ResultDTO<DataDTO<List<ProjectEnvironmentMonitorDataModel>>> selectPageList(RequestDTO<ProjectEnvironmentMonitorDataModel> res) {
        Page<ProjectEnvironmentMonitorDataModel> page = new Page<ProjectEnvironmentMonitorDataModel>(res.getPageNum(), res.getPageSize());
        EntityWrapper<ProjectEnvironmentMonitorDataModel> wrapper = new EntityWrapper<>();
        wrapper.eq("is_del", 0);
        wrapper.orderBy("create_time", false);
        if (!res.getKey().isEmpty()) {
            wrapper.like("specification", res.getKey(), SqlLike.DEFAULT);
        }
        List<ProjectEnvironmentMonitorDataModel> list = baseMapper.selectPage(page, wrapper);
        return new ResultDTO(true, DataDTO.factory(list, page.getTotal()));
    }
}
