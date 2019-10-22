package com.xingyun.equipment.crane.modular.infromation.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.core.enums.OperationEnum;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.infromation.dao.ProjectLiftDataModelMapper;
import com.xingyun.equipment.crane.modular.infromation.model.ProjectLiftDataModel;
import com.xingyun.equipment.crane.modular.infromation.service.IProjectLiftDataModelService;
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
public class ProjectLiftDataModelServiceImpl extends ServiceImpl<ProjectLiftDataModelMapper, ProjectLiftDataModel> implements IProjectLiftDataModelService {

    @Override
    public ResultDTO<Object> insertModel(RequestDTO<ProjectLiftDataModel> request) {
        ProjectLiftDataModel body = request.getBody();
        EntityWrapper<ProjectLiftDataModel> ew = new EntityWrapper<ProjectLiftDataModel>();
        ew.eq("is_del", 0);
        ew.eq("specification", body.getSpecification());
        ew.eq("manufactor", body.getManufactor());
        ProjectLiftDataModel t = new ProjectLiftDataModel();
        t = baseMapper.selectByName(ew);
        if (t == null) {
            body.setIsDel(0);
            baseMapper.insert(body);
            return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
        } else {
            throw new RuntimeException("规格型号重复");
        }
    }

    @Override
    public ResultDTO<Object> updateModel(RequestDTO<ProjectLiftDataModel> request) {
        ProjectLiftDataModel body = request.getBody();
        EntityWrapper<ProjectLiftDataModel> ew = new EntityWrapper<ProjectLiftDataModel>();
        ew.eq("is_del", 0);
        ew.eq("specification", body.getSpecification());
        ew.eq("manufactor", body.getManufactor());
        ProjectLiftDataModel t = baseMapper.selectByName(ew);

        EntityWrapper<ProjectLiftDataModel> ew2 = new EntityWrapper<ProjectLiftDataModel>();
        ew2.eq("is_del", 0);
        ew2.eq("id", body.getId());
        ProjectLiftDataModel t2 = baseMapper.selectByName(ew2);

        if (t != null && (!t2.getSpecification().equals(body.getSpecification()) || !t2.getManufactor().equals(body.getManufactor()))) {
            return new ResultDTO<>(false);

        } else {
            EntityWrapper<ProjectLiftDataModel> ew3 = new EntityWrapper<ProjectLiftDataModel>();
            ew3.eq("id", body.getId());
            baseMapper.update(body, ew3);
            return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
        }
    }

    @Override
    public ResultDTO<DataDTO<List<ProjectLiftDataModel>>> selectPageList(RequestDTO<ProjectLiftDataModel> res) {
        Page<ProjectLiftDataModel> page = new Page<ProjectLiftDataModel>(res.getPageNum(), res.getPageSize());
        EntityWrapper<ProjectLiftDataModel> wrapper = new EntityWrapper<>();
        wrapper.eq("is_del", 0);
        wrapper.orderBy("create_time", false);
        if (!res.getKey().isEmpty()) {
            wrapper.like("specification", res.getKey(), SqlLike.DEFAULT);
        }
        List<ProjectLiftDataModel> list = baseMapper.selectPage(page, wrapper);
        return new ResultDTO(true, DataDTO.factory(list, page.getTotal()));
    }
}
