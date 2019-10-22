package com.xingyun.equipment.crane.modular.infromation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.crane.modular.infromation.model.ProjectTargetSetElectric;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

/**
 * <p>
 *  电表指标设置
 * </p>
 *
 * @author hy
 * @since 2018-09-27
 */
public interface ProjectTargetSetElectricMapper extends BaseMapper<ProjectTargetSetElectric> {

	/**
	 * 分页查询
	 * @param rowBounds
	 * @param ew
	 * @return
	 */
	List<ProjectTargetSetElectric> selectPageList(
            RowBounds rowBounds, @Param("ew") EntityWrapper<RequestDTO<ProjectTargetSetElectric>> ew);
	/**
	 * 判重
	 * @param request
	 * @return
	 */
	int checkBySpecificationAndManufactor(
            @Param("e") RequestDTO<ProjectTargetSetElectric> request);

}
