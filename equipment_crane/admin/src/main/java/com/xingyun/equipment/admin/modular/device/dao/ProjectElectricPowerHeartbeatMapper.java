package com.xingyun.equipment.admin.modular.device.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.xingyun.equipment.admin.modular.device.model.ProjectElectricPowerHeartbeat;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-09-29
 */
public interface ProjectElectricPowerHeartbeatMapper extends BaseMapper<ProjectElectricPowerHeartbeat> {

	/**
	 * 分页
	 * @param rowBounds
	 * @param body
	 * @return
	 */
	List<ProjectElectricPowerHeartbeat> selectPageList(RowBounds rowBounds,@Param("ew")ProjectElectricPowerHeartbeat body);

}
