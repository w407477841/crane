package com.xywg.equipmentmonitor.modular.infromation.service;

import java.util.List;

import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectTargetSetElectric;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  电表指标设置
 * </p>
 *
 * @author hy
 * @since 2018-09-27
 */
public interface ProjectTargetSetElectricService extends IService<ProjectTargetSetElectric> {
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	ResultDTO<DataDTO<List<ProjectTargetSetElectric>>> getPageList(
			RequestDTO<ProjectTargetSetElectric> request);
	/**
	 * 判重
	 * @param request
	 * @return
	 */
	ResultDTO<DataDTO<List<ProjectTargetSetElectric>>> checkBySpecificationAndManufactor(
			RequestDTO<ProjectTargetSetElectric> request);

}
