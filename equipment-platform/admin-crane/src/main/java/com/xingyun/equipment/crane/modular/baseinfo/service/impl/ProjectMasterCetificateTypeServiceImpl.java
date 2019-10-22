package com.xingyun.equipment.crane.modular.baseinfo.service.impl;

import com.xingyun.equipment.crane.modular.baseinfo.model.ProjectMasterCetificateType;
import com.xingyun.equipment.crane.modular.baseinfo.dao.ProjectMasterCetificateTypeMapper;
import com.xingyun.equipment.crane.modular.baseinfo.service.IProjectMasterCetificateTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-08-28
 */
@Service
public class ProjectMasterCetificateTypeServiceImpl extends ServiceImpl<ProjectMasterCetificateTypeMapper, ProjectMasterCetificateType> implements IProjectMasterCetificateTypeService {

    @Override
    public List<ProjectMasterCetificateType> selectCetificateType() throws Exception {
        return baseMapper.selectCetificateType();
    }
}
