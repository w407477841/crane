package com.xywg.equipmentmonitor.modular.infromation.controller;

import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum;
import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectLiftDataModel;
import com.xywg.equipmentmonitor.modular.infromation.service.IProjectLiftDataModelService;
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
@Api(tags= {"升降机数据模板"})
@RestController
@RequestMapping("/ssdevice/information/projectLiftDataModel")
public class ProjectLiftDataModelController extends BaseController<ProjectLiftDataModel,IProjectLiftDataModelService> {

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
	ResultDTO<Object> insertModel(@RequestBody RequestDTO<ProjectLiftDataModel> request){
	    try {
	    	return service.insertModel(request);
		} catch (Exception e) {
	    	e.printStackTrace();
	    	if("规格型号重复".equals(e.getMessage())) {
				return ResultDTO.factory(ResultCodeEnum.STANDARD_REPEAT);
			}

		}
		return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
	}

	/**
	 * 修改
	 * @param request
	 * @return
	 */
	@PostMapping("updateModel")
	@ApiOperation("修改")
	ResultDTO<Object> updateModel(@RequestBody RequestDTO<ProjectLiftDataModel> request){
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
	ResultDTO<DataDTO<List<ProjectLiftDataModel>>> selectPageList(@RequestBody RequestDTO<ProjectLiftDataModel> request){
		return service.selectPageList(request);
	}

}
