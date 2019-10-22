package com.xingyun.equipment.admin.modular.device.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.device.dto.ProjectEnvironmentMessageDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectCrane;
import com.xingyun.equipment.admin.modular.device.vo.ProjectCraneVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xss
 * @since 2018-08-22
 */
public interface ProjectEnvironmentMessageMapper extends BaseMapper<ProjectEnvironmentMessageDTO> {


}
