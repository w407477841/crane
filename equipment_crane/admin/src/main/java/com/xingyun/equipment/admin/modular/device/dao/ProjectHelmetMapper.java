package com.xingyun.equipment.admin.modular.device.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectHelmet;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 * 安全帽 Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
public interface ProjectHelmetMapper extends BaseMapper<ProjectHelmet> {
    /**
     * 分页查询
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<ProjectHelmet> selectPageList(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO<ProjectHelmet>> wrapper);

    void updateStatusByIds(@Param("type")Integer type,@Param("ids")List ids);


    void updateCurrentFlagAndStatusByIds(@Param("ids")List ids);


}
