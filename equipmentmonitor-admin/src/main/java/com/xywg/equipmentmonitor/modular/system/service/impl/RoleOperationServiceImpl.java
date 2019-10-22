package com.xywg.equipmentmonitor.modular.system.service.impl;

import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.system.dao.OperationMapper;
import com.xywg.equipmentmonitor.modular.system.model.Operation;
import com.xywg.equipmentmonitor.modular.system.model.RoleOperation;
import com.xywg.equipmentmonitor.modular.system.dao.RoleOperationMapper;
import com.xywg.equipmentmonitor.modular.system.service.IRoleOperationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.modular.system.vo.OperationVo;
import com.xywg.equipmentmonitor.modular.system.vo.RoleOperationVo;
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
