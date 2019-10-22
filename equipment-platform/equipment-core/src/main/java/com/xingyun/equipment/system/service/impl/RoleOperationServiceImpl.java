package com.xingyun.equipment.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.system.dao.RoleOperationMapper;
import com.xingyun.equipment.system.model.RoleOperation;
import com.xingyun.equipment.system.service.IRoleOperationService;
import com.xingyun.equipment.system.vo.RoleOperationVo;
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
