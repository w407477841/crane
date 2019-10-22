package com.xingyun.equipment.admin.modular.infromation.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectLift;
import com.xingyun.equipment.admin.modular.infromation.dao.ProjectTargetSetLiftMapper;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectTargetSetLift;
import com.xingyun.equipment.admin.modular.infromation.service.ProjectTargetSetLiftService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xss
 * @since 2018-08-21
 */
@Service
public class ProjectTargetSetLiftServiceImpl extends ServiceImpl<ProjectTargetSetLiftMapper, ProjectTargetSetLift> implements ProjectTargetSetLiftService {

    /**
     *@Description: 条件分页查询
     *@Author xieshuaishuai
     *@Date 2018/8/21 11:22
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectTargetSetLift>>> selectPageList(RequestDTO<ProjectTargetSetLift> request) {
        Page<ProjectTargetSetLift> page = new Page<ProjectTargetSetLift>(request.getPageNum(), request.getPageSize());
        EntityWrapper<RequestDTO> ew = new EntityWrapper<RequestDTO>();
        ew.eq("a.is_del",0);
                if (null != request.getKey() && !request.getKey().isEmpty()) {
            ew.like("a.specification", request.getKey());
        }
        List<ProjectTargetSetLift> list = baseMapper.selectPageList(page,ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    /**
     *@Description: 判重
     *@Author xieshuaishuai
     *@Date 2018/8/21 14:19
     */
    @Override
    public ResultDTO<DataDTO<List<ProjectTargetSetLift>>> checkBySpecificationAndManufactor(RequestDTO<ProjectTargetSetLift> request) {
        return new ResultDTO(true, baseMapper.checkBySpecificationAndManufactor(request));
    }

    @Override
    public Integer plusCallTimes(Map<String, Object> map) throws Exception {
        return baseMapper.plusCallTimes(map);
    }

    @Override
    public Integer minusCallTimes(List<ProjectLift> projectLift) {
        return baseMapper.minusCallTimes(projectLift);
    }
}
