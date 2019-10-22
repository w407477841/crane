package com.xingyun.equipment.admin.modular.infromation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectMessageModel;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@Mapper
public interface ProjectMessageModelMapper extends BaseMapper<ProjectMessageModel> {
	/**
	 * 自动生成编码
	 * @param value
	 * @return
	 */
	String getDocumentCode(Integer value);
	/**
	 * 分页查询
	 * @param rowBounds
	 * @param wrapper
	 * @return
	 */
	List<ProjectMessageModel> selectPageList(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO<ProjectMessageModel>> wrapper);
	/**
	 * 查询单条
	 * @param id
	 * @return
	 */
	ProjectMessageModel getOne(@Param("id") Integer id);
	/**
	 * 新增占用
	 * @param deviceType
	 * @return
	 */
	int plusCallTimes(@Param("deviceType")Integer deviceType);
	
	/**
	 * 减少占用
	 * @param deviceType
	 * @return
	 */
	int minusCallTimes(@Param("deviceType")Integer deviceType);
}
