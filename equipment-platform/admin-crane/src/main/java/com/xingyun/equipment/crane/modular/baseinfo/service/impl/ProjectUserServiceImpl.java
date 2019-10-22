package com.xingyun.equipment.crane.modular.baseinfo.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.crane.modular.baseinfo.model.ProjectUser;
import com.xingyun.equipment.crane.modular.baseinfo.dao.ProjectUserMapper;
import com.xingyun.equipment.crane.modular.baseinfo.service.IProjectUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.crane.modular.baseinfo.vo.ProjectUserVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Service
public class ProjectUserServiceImpl extends ServiceImpl<ProjectUserMapper, ProjectUser> implements IProjectUserService {

    @Override
    public List<ProjectUser> selectUser(RequestDTO<ProjectUser> requestDTO) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("orgIds",requestDTO.getOrgIds());
        return baseMapper.selectUser(map);
    }

    @Override
    public List<ProjectUserVo> selectUserInfo(Page<ProjectUserVo> page, RequestDTO<ProjectUserVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("orgIds",requestDTO.getOrgIds());
        map.put("key",requestDTO.getKey());
        return baseMapper.selectUserInfo(page,map);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean insert(ProjectUser projectUser) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("identityCode",projectUser.getIdentityCode());
        if(baseMapper.selectProjectByCode(map).size() > 0) {
            throw new RuntimeException("身份证号重复");
        }
        return retBool(this.baseMapper.insert(projectUser));
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean updateById(ProjectUser projectUser) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("identityCode",projectUser.getIdentityCode());
        map.put("id",projectUser.getId());
        if(baseMapper.selectProjectByCode(map).size() > 0) {
            throw new RuntimeException("身份证号重复");
        }
        return retBool(this.baseMapper.updateById(projectUser));
    }
}
