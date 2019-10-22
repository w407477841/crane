package com.xingyun.equipment.crane.modular.device.dao;

import com.xingyun.equipment.crane.modular.device.dto.ProjectEnvironmentMessageDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectMessageElectric;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-09-28
 */
public interface ProjectMessageElectricMapper extends BaseMapper<ProjectMessageElectric> {

	void insert(ProjectEnvironmentMessageDTO projectEnvironmentMessageDTO);

}
