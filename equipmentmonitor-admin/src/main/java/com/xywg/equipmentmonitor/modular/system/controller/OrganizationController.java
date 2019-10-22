package com.xywg.equipmentmonitor.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.security.service.SecurityService;
import com.xywg.equipmentmonitor.modular.system.model.OrganizationUser;
import com.xywg.equipmentmonitor.modular.system.model.User;
import com.xywg.equipmentmonitor.modular.system.service.IOrganizationUserService;
import com.xywg.equipmentmonitor.modular.system.vo.OrganizationUserVo;
import com.xywg.equipmentmonitor.modular.system.vo.OrganizationVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.modular.system.model.Organization;
import com.xywg.equipmentmonitor.modular.system.service.IOrganizationService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ssdevice/system/organization")
public class OrganizationController extends BaseController<Organization, IOrganizationService> {
	@Autowired
	SecurityService   securityService;

	@Autowired
	IOrganizationUserService iOrganizationUserService;

	@Override
	public String insertRole() {
		return "system:org:insert";
	}

	@Override
	public String updateRole() {
		return "system:org:update";
	}

	@Override
	public String deleteRole() {
		return "system:org:delete";
	}

	@Override
	public String viewRole() {
		return "system:org:view";
	}

	@ApiOperation("获取部门信息")
	@PostMapping(value = "/selectOrganizationInfo")
	public ResultDTO<List<OrganizationVo>> selectOrganizationInfo(@RequestBody RequestDTO<Organization> t) {
		try {
			return new ResultDTO<>(true,service.selectOrganizationInfo(t));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}
	@ApiOperation("选择集团")
	@PostMapping(value =  "chooseOrg")
	public ResultDTO<Integer> chooseOrg(@RequestBody  RequestDTO<Integer> t){
		try {
			//删除当前用户之前选择的集团
			securityService.removeOrgids(Const.currUser.get().getId());

			securityService.updateOrgId(
					Const.token.get(),
					t.getBody()
						);
			securityService.updateOrgids(Const.currUser.get().getId());
			return new ResultDTO<>(true);
		}catch (Exception e){
			e.printStackTrace();
		}
		return  new ResultDTO<>(false);
	}

	@ApiOperation("删除部门用户")
	@PostMapping(value = "/deleteOrgUser")
	public ResultDTO<OrganizationUser> deleteOrgUser(@RequestBody RequestDTO<OrganizationUser> requestDTO) {
		try {
			String[] id = requestDTO.getIds().get(0).toString().split(",");
			List<Integer> ids = new ArrayList<>(10);
			for(int i = 0;i < id.length;i++) {
				ids.add(Integer.parseInt(id[i]));
			}
			if(iOrganizationUserService.deleteBatchIds(ids)) {
				return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResultDTO.resultFactory(OperationEnum.DELETE_ERROR);
	}

	@ApiOperation("新增部门用户")
	@PostMapping(value = "/insertOrgUser")
	public ResultDTO<OrganizationUserVo> insertOrgUser(@RequestBody RequestDTO<OrganizationUserVo> requestDTO) {
		try {
			List<OrganizationUser> list = requestDTO.getBody().getOrgUsers();
			List<OrganizationUser> organizationUsers = requestDTO.getBody().getOrgUsers();
			for(int i = 0;i < list.size();i++) {
				Wrapper<OrganizationUser> wrapper = new EntityWrapper<>();
				wrapper.eq("org_id",list.get(i).getOrgId());
				wrapper.eq("user_id",list.get(i).getUserId());
				List<OrganizationUser> organizationUserList = iOrganizationUserService.selectList(wrapper);
				if(organizationUserList.size() > 0) {
					organizationUsers.remove(i);
				}
			}
			if(organizationUsers.size() > 0) {
				if(iOrganizationUserService.insertBatch(organizationUsers)) {
					return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
	}

	@PostMapping("/selectOrganization")
	@ApiOperation("获取当前用户下项目部")
	public ResultDTO<List<Organization>> selectOrganization(@RequestBody RequestDTO<Organization> requestDTO) {
		try {
			return new ResultDTO<>(true,service.selectOrganization(Const.orgIds.get()));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}

	@PostMapping("/selectUnderOrganization")
	@ApiOperation("获取当前用户下项目部")
	public ResultDTO<List<Organization>> selectUnderOrganization(@RequestBody RequestDTO<Organization> requestDTO) {
		try {
			return new ResultDTO<>(true,service.selectUnderOrganization());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}
}
