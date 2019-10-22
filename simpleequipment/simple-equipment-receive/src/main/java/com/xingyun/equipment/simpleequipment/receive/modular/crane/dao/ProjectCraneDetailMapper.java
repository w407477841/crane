package com.xingyun.equipment.simpleequipment.receive.modular.crane.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xingyun.equipment.simpleequipment.receive.modular.crane.model.ProjectCraneDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Mapper
public interface ProjectCraneDetailMapper extends BaseMapper<ProjectCraneDetail> {
/**
 * 新增详细数据
 * @param tableName 表名
 * @param projectCraneDetail 新增数据
 * */
	 void createDetail(@Param("t") ProjectCraneDetail projectCraneDetail, @Param("tableName") String tableName);
	
}
