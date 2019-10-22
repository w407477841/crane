package com.xingyun.equipment.crane.modular.alipay.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.crane.modular.alipay.model.ProjectTopUp;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhouyujie
 * @since 2019-06-25
 */

public interface ProjectTopUpMapper extends BaseMapper<ProjectTopUp> {

    List<ProjectTopUp> selectPageList(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO> wrapper);
}
