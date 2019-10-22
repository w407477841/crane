package com.xingyun.equipment.admin.modular.infromation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectMessageModel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@Service
public interface ProjectMessageModelService extends IService<ProjectMessageModel> {

	/**
	 * 新增
	 * @param p
	 * @return
	 */
	ResultDTO<Object> insertMessage(ProjectMessageModel p);
	/**
	 * 分页查询
	 * @param res
	 * @return
	 */
	ResultDTO<DataDTO<List<ProjectMessageModel>>> selectPageList(RequestDTO<ProjectMessageModel> res);
	/**
	 * 启用
	 * @param res
	 * @return
	 */
	ResultDTO<Object> updateStatus(RequestDTO<ProjectMessageModel> res);
	
	/**
	 * 查询单条
	 * @param id
	 * @return
	 */
	ResultDTO<ProjectMessageModel> getOne(Integer id);
}
