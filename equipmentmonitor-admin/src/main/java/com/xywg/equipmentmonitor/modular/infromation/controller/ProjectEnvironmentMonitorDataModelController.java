package com.xywg.equipmentmonitor.modular.infromation.controller;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectEnvironmentMonitorDataModel;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectMessageModel;
import com.xywg.equipmentmonitor.modular.infromation.service.IProjectEnvironmentMonitorDataModelService;
import com.xywg.equipmentmonitor.modular.infromation.service.ProjectMessageModelService;
import com.xywg.equipmentmonitor.modular.infromation.service.impl.ProjectEnvironmentMonitorDataModelServiceImpl;
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
@Api(tags= {"扬尘数据模板"})
@RestController
@RequestMapping("ssdevice/information/projectEnvironmentMonitorDataModel")
public class ProjectEnvironmentMonitorDataModelController extends BaseController<ProjectEnvironmentMonitorDataModel,IProjectEnvironmentMonitorDataModelService> {

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
	ResultDTO<Object> insertModel(@RequestBody RequestDTO<ProjectEnvironmentMonitorDataModel> request){
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
	ResultDTO<Object> updateModel(@RequestBody RequestDTO<ProjectEnvironmentMonitorDataModel> request){
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
	ResultDTO<DataDTO<List<ProjectEnvironmentMonitorDataModel>>> selectPageList(@RequestBody RequestDTO<ProjectEnvironmentMonitorDataModel> request){
		return service.selectPageList(request);
	}

}
