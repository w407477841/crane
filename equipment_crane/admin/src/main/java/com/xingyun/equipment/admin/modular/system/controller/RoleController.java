package com.xingyun.equipment.admin.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.common.constant.OperationEnum;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.system.model.RoleOperation;
import com.xingyun.equipment.admin.modular.system.service.IRoleOperationService;
import com.xingyun.equipment.admin.modular.system.vo.RoleOperationVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.modular.system.model.Role;
import com.xingyun.equipment.admin.modular.system.service.IRoleService;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/***
 *@author:jixiaojun
 *DATE:2018/8/20
 *TIME:20:15
 */
@RestController()
@RequestMapping("/ssdevice/system/role")
public class RoleController extends BaseController<Role, IRoleService>{
	@Autowired
	IRoleOperationService iRoleOperationService;

	@Override
	public String insertRole() {
		return "system:role:insert";
	}

	@Override
	public String updateRole() {
		return "system:role:update";
	}

	@Override
	public String deleteRole() {
		return "system:role:delete";
	}

	@Override
	public String viewRole() {
		return "system:role:view";
	}

	@ApiOperation("获取角色")
	@PostMapping("/selectRoleInfo")
	public ResultDTO<DataDTO<List<Role>>> selectRoleInfo(@RequestBody RequestDTO<Role> requestDTO) {
		try {
			Page<Role> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
			List<Role> list = service.selectRoleInfo(page,requestDTO);
			return new ResultDTO<>(true,DataDTO.factory(list,page.getTotal()));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}

	@ApiOperation("新增角色权限")
	@PostMapping("/insertRoleOperation")
	public ResultDTO<RoleOperation> insertRoleOperation(@RequestBody RequestDTO<RoleOperationVo> requestDTO) {
		try {
			if(requestDTO.getBody().getRoleOperations().size() > 0) {
				Wrapper<RoleOperation> wrapper = new EntityWrapper<>();
				wrapper.eq("role_id",requestDTO.getId());
				iRoleOperationService.delete(wrapper);
				if(requestDTO.getBody().getRoleOperations().size() > 0) {
					if(iRoleOperationService.insertBatch(requestDTO.getBody().getRoleOperations())) {
						return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
	}

	@ApiOperation("获取角色拥有的权限")
	@PostMapping("/selectRoleOwnedOperation")
	public ResultDTO<List<Integer>> selectRoleOwnedOperation(@RequestBody RequestDTO<RoleOperationVo> requestDTO) {
		try {
			List<RoleOperation> roleOperations = iRoleOperationService.selectRoleOwnedOperation(requestDTO);
			List<Integer> operIds = new ArrayList<>(10);
			for(int i = 0;i < roleOperations.size();i++) {
				operIds.add(roleOperations.get(i).getOperId());
			}
			return new ResultDTO<>(true,operIds);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}
}
