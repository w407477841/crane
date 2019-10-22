package com.xingyun.equipment.admin.modular.system.service.impl;

import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.system.dao.OperationMapper;
import com.xingyun.equipment.admin.modular.system.model.Operation;
import com.xingyun.equipment.admin.modular.system.model.RoleOperation;
import com.xingyun.equipment.admin.modular.system.dao.RoleOperationMapper;
import com.xingyun.equipment.admin.modular.system.service.IRoleOperationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.admin.modular.system.vo.OperationVo;
import com.xingyun.equipment.admin.modular.system.vo.RoleOperationVo;
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
public class RoleOperationServiceImpl extends ServiceImpl<RoleOperationMapper, RoleOperation> implements IRoleOperationService {
    @Override
    public List<RoleOperation> selectRoleOwnedOperation(RequestDTO<RoleOperationVo> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("roleId",requestDTO.getBody().getRoleId());
        return baseMapper.selectRoleOwnedOperation(map);
    }
}
