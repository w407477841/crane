package com.xywg.equipmentmonitor.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.system.dao.OperationMapper;
import com.xywg.equipmentmonitor.modular.system.dao.RoleOperationMapper;
import com.xywg.equipmentmonitor.modular.system.model.Operation;
import com.xywg.equipmentmonitor.modular.system.model.Role;
import com.xywg.equipmentmonitor.modular.system.dao.RoleMapper;
import com.xywg.equipmentmonitor.modular.system.model.RoleOperation;
import com.xywg.equipmentmonitor.modular.system.service.IRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.modular.system.vo.OperationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Override
    public List<Role> selectRoleInfo(Page<Role> page,RequestDTO<Role> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("orgIds",requestDTO.getOrgIds());
        map.put("key",requestDTO.getKey());
        return baseMapper.selectRoleInfo(page,map);
    }
}
