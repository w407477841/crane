package com.xingyun.equipment.admin.modular.infromation.controller;

import com.xingyun.equipment.admin.core.controller.BaseController;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectCraneDataModel;
import com.xingyun.equipment.admin.modular.infromation.service.IProjectCraneDataModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
*Description:
*Company:星云网格
*@author yuanyang
*@date 2018年8月24日
*/
@Api(tags= {"塔吊数据模板"})
@RestController
@RequestMapping("ssdevice/information/projectCraneDataModel")
public class ProjectCraneDataModelController extends BaseController<ProjectCraneDataModel,IProjectCraneDataModelService> {

	@Override
	public String insertRole() {
		return null;
	}

	@Override
	public String updateRole() {
		return null;
	}

	@Override
	public String deleteRole() {
		return null;
	}

	@Override
	public String viewRole() {
		return null;
	}
	/**
	 * 新增
	 * @param request
	 * @return
	 */
	@PostMapping("insertModel")
	@ApiOperation("新增")
	ResultDTO<Object> insertModel(@RequestBody RequestDTO<ProjectCraneDataModel> request){
		try {
			return service.insertModel(request);
		} catch (Exception e) {
			return new ResultDTO<>(false);
		}
		
	}

	/**
	 * 修改
	 * @param request
	 * @return
	 */
	@PostMapping("updateModel")
	@ApiOperation("修改")
	ResultDTO<Object> updateModel(@RequestBody RequestDTO<ProjectCraneDataModel> request){
		try {
			return service.updateModel(request);
		} catch (Exception e) {
			return new ResultDTO<>(false);
		}

	}

	/**
	 * 分页查询
	 * @param request
	 * @return
	 * @author yuanyang
	 */
	@ApiOperation("列表")
	@PostMapping("selectPageList")
	ResultDTO<DataDTO<List<ProjectCraneDataModel>>> selectPageList(@RequestBody RequestDTO<ProjectCraneDataModel> request){
		return service.selectPageList(request);
	}

}
