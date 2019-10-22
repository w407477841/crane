package com.xingyun.equipment.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.Const;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.core.enums.OperationEnum;
import com.xingyun.equipment.system.model.Organization;
import com.xingyun.equipment.system.model.OrganizationUser;
import com.xingyun.equipment.system.service.IOrganizationService;
import com.xingyun.equipment.system.service.IOrganizationUserService;
import com.xingyun.equipment.system.service.SecurityService;
import com.xingyun.equipment.system.vo.OrganizationUserVo;
import com.xingyun.equipment.system.vo.OrganizationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin-login/system/organization")
public class OrganizationController extends BaseController<Organization, IOrganizationService> {
	@Autowired
	SecurityService securityService;

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

	@PostMapping(value = "/selectOrganizationInfo")
	public ResultDTO<List<OrganizationVo>> selectOrganizationInfo(@RequestBody RequestDTO<Organization> t) {
		try {
			return new ResultDTO<>(true,service.selectOrganizationInfo(t));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}
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

	@PostMapping(value = "/deleteOrgUser")
	public ResultDTO<OrganizationUser> deleteOrgUser(@RequestBody RequestDTO<OrganizationUser> requestDTO) {
		try {
			List<Integer> ids = requestDTO.getIds();
			if(iOrganizationUserService.deleteBatchIds(ids)) {
				return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResultDTO.resultFactory(OperationEnum.DELETE_ERROR);
	}

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
	public ResultDTO<List<Organization>> selectOrganization(@RequestBody RequestDTO<Organization> requestDTO) {
		try {
			return new ResultDTO<>(true,service.selectOrganization(Const.orgIds.get()));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}

	@PostMapping("/selectUnderOrganization")
	public ResultDTO<List<Organization>> selectUnderOrganization(@RequestBody RequestDTO<Organization> requestDTO) {
		try {
			return new ResultDTO<>(true,service.selectUnderOrganization());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}
}
