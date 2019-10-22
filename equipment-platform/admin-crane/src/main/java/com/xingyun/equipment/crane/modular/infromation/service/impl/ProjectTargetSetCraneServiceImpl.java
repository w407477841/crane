package com.xingyun.equipment.crane.modular.infromation.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.infromation.dao.ProjectTargetSetCraneMapper;
import com.xingyun.equipment.crane.modular.infromation.model.ProjectTargetSetCrane;
import com.xingyun.equipment.crane.modular.infromation.service.ProjectTargetSetCraneService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xss
 * @since 2018-08-22
 */
@Service
public class ProjectTargetSetCraneServiceImpl extends ServiceImpl<ProjectTargetSetCraneMapper, ProjectTargetSetCrane> implements ProjectTargetSetCraneService {

    /**
     *@Description:条件分页查询
     *@Author xieshuaishuai
     *@Date 2018/8/30 20:31
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectTargetSetCrane>>> selectPageList(RequestDTO<ProjectTargetSetCrane> request) {
        Page<ProjectTargetSetCrane> page = new Page<ProjectTargetSetCrane>(request.getPageNum(), request.getPageSize());
        EntityWrapper<RequestDTO> ew = new EntityWrapper<RequestDTO>();
        ew.eq("a.is_del",0);
        if (null != request.getKey() && !request.getKey().isEmpty()) {
            ew.like("a.specification", request.getKey());
        }
        List<ProjectTargetSetCrane> list = baseMapper.selectPageList(page,ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    /**
     *@Description:判重
     *@Author xieshuaishuai
     *@Date 2018/8/30 20:31
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectTargetSetCrane>>> checkBySpecificationAndManufactor(RequestDTO<ProjectTargetSetCrane> request) {
        return new ResultDTO(true,baseMapper.checkBySpecificationAndManufactor(request));
    }
}
