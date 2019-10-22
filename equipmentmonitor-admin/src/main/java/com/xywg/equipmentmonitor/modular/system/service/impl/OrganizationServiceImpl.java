package com.xywg.equipmentmonitor.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.security.service.SecurityService;
import com.xywg.equipmentmonitor.core.util.TreeUtils;
import com.xywg.equipmentmonitor.core.vo.OrgVO;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.system.model.Organization;
import com.xywg.equipmentmonitor.modular.system.dao.OrganizationMapper;
import com.xywg.equipmentmonitor.modular.system.model.User;
import com.xywg.equipmentmonitor.modular.system.service.IOrganizationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.modular.system.vo.OrganizationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-16
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {
    @Autowired
    SecurityService securityService;
    @Override
    public List<OrganizationVo> selectOrganizationInfo(RequestDTO<Organization> requestDTO) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("orgIds",requestDTO.getOrgIds());
        List<OrganizationVo> list = baseMapper.selectOrganizationInfo(map);
        TreeUtils<OrganizationVo> treeUtils = new TreeUtils<>();
        treeUtils.rootDir(list);
        return treeUtils.getTreeList();
    }

    @Override
    public boolean insert(Organization entity) {
        boolean isSuccess = super.insert(entity);
        securityService.updateOrgids(Const.currUser.get().getId());
        return isSuccess;
    }

    @Override
    public List<Organization> getByUserId(Integer id) {
        List<Organization> orgs =   baseMapper.getByUserId(id);
        return orgs;
    }

    @Override
    public List<Integer> getOrgsByParent(Integer parentId) {

        List<OrgVO>  orgs =   baseMapper.selectALlOrg();
        TreeUtils<OrgVO> utils= new TreeUtils<OrgVO>();
           utils.rootDirWithIds(orgs,parentId);
          orgs  =    utils.getList(true);
         List<Integer>  orgids =  new ArrayList<>();
         for(OrgVO org:orgs){
             orgids.add(org.getId());
         }
        return orgids;
    }

    @Override
    public List<Organization> selectOrganization(List<Integer> ids) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("list",ids);
        return baseMapper.selectOrganization(map);
    }

    @Override
    public List<Organization> selectUnderOrganization() throws Exception {
        List<Integer> ids = Const.orgIds.get();
        Wrapper<Organization> wrapper = new EntityWrapper<>();
        wrapper.in("id",ids);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean deleteById(Serializable id) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("orgIds", Const.orgIds.get());
        List<OrganizationVo> list = baseMapper.selectOrganizationInfo(map);
        deleteChildren(id,list);
        return super.deleteById(id);
    }

    public void deleteChildren(Serializable id,List<OrganizationVo> organizationVos) {
        for(int i = 0;i < organizationVos.size();i++) {
            if(id.equals(organizationVos.get(i).getPid())) {
                deleteChildren(organizationVos.get(i).getId(),organizationVos);
                baseMapper.deleteById(organizationVos.get(i).getId());
            }
        }
    }
}
