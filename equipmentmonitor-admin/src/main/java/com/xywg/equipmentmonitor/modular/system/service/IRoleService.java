package com.xywg.equipmentmonitor.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.system.model.Role;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.modular.system.model.RoleOperation;
import com.xywg.equipmentmonitor.modular.system.vo.OperationVo;

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
