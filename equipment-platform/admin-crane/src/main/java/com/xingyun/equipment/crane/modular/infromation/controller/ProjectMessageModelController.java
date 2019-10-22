package com.xingyun.equipment.crane.modular.infromation.controller;

import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.infromation.model.ProjectMessageModel;
import com.xingyun.equipment.crane.modular.infromation.service.ProjectMessageModelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年8月21日 
*/
@Api(tags= {"短信推送"})
@RestController
@RequestMapping("/admin-crane/information/projectMessage")
public class ProjectMessageModelController extends BaseController<ProjectMessageModel,ProjectMessageModelService> {

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
	 * @param p
	 * @return
	 */
	@PostMapping("insertMessage")
	@ApiOperation("新增")
	ResultDTO<Object> insertMessage(@RequestBody RequestDTO<ProjectMessageModel> p){
	    try {
	    	System.out.println(p.getBody());
	    	return service.insertMessage(p.getBody());
		} catch (Exception e) {
			return new ResultDTO<>(false);
		}
		
	}
	
	/**
	 * 分页查询
	 * @param res
	 * @return
	 */
	@ApiOperation("一览")
	@PostMapping("selectPageList")
	ResultDTO<DataDTO<List<ProjectMessageModel>>> selectPageList(@RequestBody RequestDTO<ProjectMessageModel> res){
		return service.selectPageList(res);
	}
	/**
	 * 启用
	 * @param res
	 * @return
	 */
	@ApiOperation("启用")
	@PostMapping("updateStatus")
	public ResultDTO<Object> updateStatus(@RequestBody RequestDTO<ProjectMessageModel> res) {
		try {
			return service.updateStatus(res);
		} catch (Exception e) {
			return new ResultDTO<>(false);
		}
	}
	/**
	 * 单条
	 * @param res
	 * @return
	 */
	@ApiOperation("单条")
	@PostMapping("getOne")
	public ResultDTO<ProjectMessageModel> getOne(@RequestBody RequestDTO<ProjectMessageModel> res) {
		return service.getOne(Integer.valueOf(res.getId().toString()));
	}

	@ApiOperation("不分页已启用列表")
	@PostMapping("/selectUseList")
	public ResultDTO<List<ProjectMessageModel>> selectUseList(@RequestBody RequestDTO<ProjectMessageModel> requestDTO) {
		try {
			Wrapper<ProjectMessageModel> wrapper = new EntityWrapper<>();
			wrapper.eq("status",1);
			return new ResultDTO<>(true,service.selectList(wrapper));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}
}
