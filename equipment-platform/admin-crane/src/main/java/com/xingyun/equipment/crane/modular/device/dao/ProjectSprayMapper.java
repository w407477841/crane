package com.xingyun.equipment.crane.modular.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectSpray;
import com.xingyun.equipment.crane.modular.device.vo.ProjectSprayVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 10:48
 */
@Mapper
public interface ProjectSprayMapper extends BaseMapper<ProjectSpray>{

    /**
     *
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<ProjectSprayVO> getPageList(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO> wrapper);

    ProjectSprayVO getById(@Param("id") Integer id);

    int checkByDeviceNo(@Param("r") RequestDTO requestDTO);
}
