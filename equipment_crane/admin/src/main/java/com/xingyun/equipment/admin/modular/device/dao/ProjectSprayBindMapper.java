package com.xingyun.equipment.admin.modular.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xingyun.equipment.admin.modular.device.model.ProjectSprayBind;
import com.xingyun.equipment.admin.modular.device.vo.ProjectSprayVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 19:46
 */
@Mapper
public interface ProjectSprayBindMapper extends BaseMapper<ProjectSprayBind>{

    List<ProjectSprayVO> getList(@Param("id")Integer id);

    int add(@Param("p")ProjectSprayBind p);

    int deleteByEnvironment(@Param("id")Integer id);
}
