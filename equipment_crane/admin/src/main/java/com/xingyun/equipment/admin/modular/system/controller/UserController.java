package com.xingyun.equipment.admin.modular.system.controller;

import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.common.constant.Const;
import com.xingyun.equipment.admin.core.common.constant.OperationEnum;
import com.xingyun.equipment.admin.core.common.constant.ResultCodeEnum;
import com.xingyun.equipment.admin.modular.system.model.UserRole;
import com.xingyun.equipment.admin.modular.system.service.IUserRoleService;
import com.xingyun.equipment.admin.modular.system.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.common.constant.PermissionEnum;
import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.system.model.User;
import com.xingyun.equipment.admin.modular.system.service.IUserService;
@RestController
@RequestMapping("/ssdevice/system/user")
public class UserController extends BaseController<User, IUserService> {
	@Autowired
	IUserRoleService iUserRoleService;

	@Override
	public String insertRole() {
		return "system:user:insert";
	}
	@Override
	public String updateRole() {
		return "system:user:update";
	}
	@Override
	public String deleteRole() {
		return "system:user:delete";
	}
	@Override
	public String viewRole() {
		return "system:user:view";
	}

	@ApiOperation("获取用户")
	@PostMapping("selectList2")
	public ResultDTO<DataDTO<List<User>>> selectList2(@RequestBody  RequestDTO<User> t){
		Page<User> page = new Page<>(t.getPageNum(), t.getPageSize());
		List<User>   ts=   service.selectPage(page).getRecords();
		return new ResultDTO<DataDTO<List<User>>>(true,DataDTO.factory(ts,page.getTotal()));
	}

	@ApiOperation("新增用户")
	@PostMapping("/insertUser")
	public ResultDTO<UserVo> insertUser(@RequestBody RequestDTO<UserVo> requestDTO) {
	 	try {
			if(service.insertUser(requestDTO.getBody())) {
				return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
			}
		}catch (Exception e) {
	 		e.printStackTrace();
	 		String message = e.getMessage();
	 		if("登录名重复".equals(message)) {
				return ResultDTO.factory(ResultCodeEnum.LOGIN_NAME_REPEAT);
			}else if("手机号重复".equals(message)) {
	 			return ResultDTO.factory(ResultCodeEnum.PHONE_REPEAT);
			}
		}
		return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
	}

	@ApiOperation("修改用户")
	@PostMapping("/updateUser")
	public ResultDTO<UserVo> updateUser(@RequestBody RequestDTO<UserVo> requestDTO) {
		try {
			if(service.updateUser(requestDTO.getBody())) {
				return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
			}
		}catch (Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			if("登录名重复".equals(message)) {
				return ResultDTO.factory(ResultCodeEnum.LOGIN_NAME_REPEAT);
			}else if("手机号重复".equals(message)) {
				return ResultDTO.factory(ResultCodeEnum.PHONE_REPEAT);
			}
		}
		return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
	}

	@ApiOperation("获取用户")
	@PostMapping("/selectUserInfo")
	public ResultDTO<DataDTO<List<User>>> selectUserInfo(@RequestBody RequestDTO<User> requestDTO) {
		try {
			Page<User> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
			List<User> list = service.selectUserInfo(page,requestDTO);
			return new ResultDTO<DataDTO<List<User>>>(true,DataDTO.factory(list,page.getTotal()));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}

	@ApiOperation("根据部门id获取用户")
	@PostMapping(value = "/selectUserByOrgId")
	public ResultDTO<DataDTO<List<User>>> selectUserByOrgId(@RequestBody  RequestDTO<User> requestDTO) {
		try {
			Page<User> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
			List<User> list = service.selectUserByOrgId(page,requestDTO);
			return new ResultDTO<>(true,DataDTO.factory(list,page.getTotal()));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}

	@ApiOperation("获取不在部门中的用户")
	@PostMapping(value = "/selectUserNotInOrg")
	public ResultDTO<DataDTO<List<User>>> selectUserNotInOrg(@RequestBody  RequestDTO<User> requestDTO) {
		try {
			Page<User> page = new Page<>(requestDTO.getPageNum(),requestDTO.getPageSize());
			List<User> list = service.selectUserNotInOrg(page,requestDTO);
			return new ResultDTO<>(true,DataDTO.factory(list,page.getTotal()));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}

	@ApiOperation("根据id获取用户")
	@PostMapping(value = "/selectUserById")
	public ResultDTO<UserVo> selectUserById(@RequestBody RequestDTO<User> requestDTO) {
		try {
			return new ResultDTO<>(true,service.selectUserById(requestDTO));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}

	@ApiOperation("修改密码")
	@PostMapping(value = "/changePWD")
	public ResultDTO<User> changePWD(@RequestBody RequestDTO<UserVo> requestDTO) {
		try {
			if(service.changePWD(requestDTO)) {
				return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
			}
		}catch (Exception e) {
			e.printStackTrace();
			if("原密码错误".equals(e.getMessage())) {
				return ResultDTO.factory(ResultCodeEnum.PASSWORD_WRONG);
			}
		}
		return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
	}

	@ApiOperation("获取人员不分页")
	@PostMapping("/selectUser")
	public ResultDTO<List<User>> selectUser(@RequestBody RequestDTO<User> requestDTO) {
		try {
			Wrapper<User> wrapper = new EntityWrapper<>();
			wrapper.in("org_id",requestDTO.getOrgIds());
			List<User> users = service.selectList(wrapper);
			return new ResultDTO<>(true,users);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}

	@ApiOperation("获取人员不分页")
	@PostMapping("/selectUserByLogin")
	public ResultDTO<List<User>> getUserByLogin() {
		try {
			Wrapper<User> wrapper = new EntityWrapper<>();
			wrapper.in("org_id",Const.orgIds.get());
			List<User> users = service.selectList(wrapper);
			return new ResultDTO<>(true,users);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}
}
