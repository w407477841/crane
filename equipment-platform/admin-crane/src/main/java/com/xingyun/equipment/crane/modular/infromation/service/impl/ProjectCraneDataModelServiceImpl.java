package com.xingyun.equipment.crane.modular.infromation.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.core.enums.OperationEnum;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.infromation.dao.ProjectCraneDataModelMapper;
import com.xingyun.equipment.crane.modular.infromation.model.ProjectCraneDataModel;
import com.xingyun.equipment.crane.modular.infromation.service.IProjectCraneDataModelService;
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
public class ProjectCraneDataModelServiceImpl extends ServiceImpl<ProjectCraneDataModelMapper, ProjectCraneDataModel> implements IProjectCraneDataModelService {

    @Override
    public ResultDTO<Object> insertModel(RequestDTO<ProjectCraneDataModel> request) {
        ProjectCraneDataModel body = request.getBody();
        EntityWrapper<ProjectCraneDataModel> ew = new EntityWrapper<ProjectCraneDataModel>();
        ew.eq("is_del", 0);
        ew.eq("specification", body.getSpecification());
        ew.eq("manufactor", body.getManufactor());
        ProjectCraneDataModel t = new ProjectCraneDataModel();
        t = baseMapper.selectByName(ew);
        if (t == null) {
            body.setIsDel(0);
            baseMapper.insert(body);
            return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
        } else {
            return new ResultDTO<>(false, null, "该厂商规格型号模板已存在！");
        }
    }

    @Override
    public ResultDTO<Object> updateModel(RequestDTO<ProjectCraneDataModel> request) {
        ProjectCraneDataModel body = request.getBody();
        EntityWrapper<ProjectCraneDataModel> ew = new EntityWrapper<ProjectCraneDataModel>();
        ew.eq("is_del", 0);
        ew.eq("specification", body.getSpecification());
        ew.eq("manufactor", body.getManufactor());
        ProjectCraneDataModel t = baseMapper.selectByName(ew);

        EntityWrapper<ProjectCraneDataModel> ew2 = new EntityWrapper<ProjectCraneDataModel>();
        ew2.eq("is_del", 0);
        ew2.eq("id", body.getId());
        ProjectCraneDataModel t2 = baseMapper.selectByName(ew2);

        if (t != null && (!t2.getSpecification().equals(body.getSpecification()) || !t2.getManufactor().equals(body.getManufactor()))) {
            return new ResultDTO<>(false, null, "该厂商规格型号模板已存在！");

        } else {
            EntityWrapper<ProjectCraneDataModel> ew3 = new EntityWrapper<ProjectCraneDataModel>();
            ew3.eq("id", body.getId());
            baseMapper.update(body, ew3);
            return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
        }
    }

    @Override
    public ResultDTO<DataDTO<List<ProjectCraneDataModel>>> selectPageList(RequestDTO<ProjectCraneDataModel> res) {
        Page<ProjectCraneDataModel> page = new Page<ProjectCraneDataModel>(res.getPageNum(), res.getPageSize());
        EntityWrapper<ProjectCraneDataModel> wrapper = new EntityWrapper<>();
        wrapper.eq("is_del", 0);
        wrapper.orderBy("create_time", false);
        if (!res.getKey().isEmpty()) {
            wrapper.like("specification", res.getKey(), SqlLike.DEFAULT);
        }
        List<ProjectCraneDataModel> list = baseMapper.selectPage(page, wrapper);
        return new ResultDTO(true, DataDTO.factory(list, page.getTotal()));
    }
}
