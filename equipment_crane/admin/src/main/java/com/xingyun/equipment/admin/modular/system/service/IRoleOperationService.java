package com.xingyun.equipment.admin.modular.system.service;

import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.system.model.RoleOperation;
import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.admin.modular.system.vo.RoleOperationVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf123
 * @since 2018-08-13
 */
public interface IRoleOperationService extends IService<RoleOperation> {
    /**
     * 获取角色拥有的权限
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<RoleOperation> selectRoleOwnedOperation(RequestDTO<RoleOperationVo> requestDTO) throws Exception;
}
