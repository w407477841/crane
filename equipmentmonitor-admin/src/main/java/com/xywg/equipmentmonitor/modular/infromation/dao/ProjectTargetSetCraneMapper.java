package com.xywg.equipmentmonitor.modular.infromation.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectTargetSetCrane;
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
public interface ProjectTargetSetCraneMapper extends BaseMapper<ProjectTargetSetCrane> {

    /**
     * 条件分页查询
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<ProjectTargetSetCrane> selectPageList(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO> wrapper);

    /**
     * 判重
     * @param request
     * @return
     */
    int checkBySpecificationAndManufactor(@Param("t") RequestDTO<ProjectTargetSetCrane> request);
}
