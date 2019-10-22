package com.xingyun.equipment.admin.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.system.model.Role;
import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.admin.modular.system.model.RoleOperation;
import com.xingyun.equipment.admin.modular.system.vo.OperationVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-16
 */
public interface IRoleService extends IService<Role> {
    /**
     * 获取角色信息
     * @param page
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<Role> selectRoleInfo(Page<Role> page,RequestDTO<Role> requestDTO) throws Exception;
}
