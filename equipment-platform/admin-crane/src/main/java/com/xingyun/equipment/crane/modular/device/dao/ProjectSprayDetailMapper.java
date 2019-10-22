package com.xingyun.equipment.crane.modular.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectSprayDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 10:52
 */
@Mapper
public interface ProjectSprayDetailMapper extends BaseMapper<ProjectSprayDetail>{

    /**
     * 查询
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<ProjectSprayDetail> getBySprayId(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO> wrapper);
}
