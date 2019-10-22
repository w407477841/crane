package com.xywg.equipmentmonitor.modular.infromation.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.infromation.dao.ProjectTargetSetEnvironmentMapper;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectTargetSetEnvironment;
import com.xywg.equipmentmonitor.modular.infromation.service.ProjectTargetSetEnvironmentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xss
 * @since 2018-08-20
 */
@Service
public class ProjectTargetSetEnvironmentServiceImpl extends ServiceImpl<ProjectTargetSetEnvironmentMapper, ProjectTargetSetEnvironment> implements ProjectTargetSetEnvironmentService {

    /**
     * 条件分页查询
     * @param request
     * @return
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectTargetSetEnvironment>>> getPageList(RequestDTO<ProjectTargetSetEnvironment> request) {
        Page<ProjectTargetSetEnvironment> page = new Page<ProjectTargetSetEnvironment>(request.getPageNum(), request.getPageSize());
        EntityWrapper<RequestDTO> ew = new EntityWrapper<RequestDTO>();
        ew.eq("a.is_del",0);
        if (null != request.getKey() && !request.getKey().isEmpty()) {
            ew.like("a.specification", request.getKey());
        }
        List<ProjectTargetSetEnvironment> list = baseMapper.selectPageList(page,ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    /**
     * 根据制造厂商和规格型号判重
     * @param request
     * @return
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectTargetSetEnvironment>>> checkBySpecificationAndManufactor(RequestDTO<ProjectTargetSetEnvironment> request) {
        return new ResultDTO(true, baseMapper.checkBySpecificationAndManufactor(request));
    }
}
