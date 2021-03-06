package com.xingyun.equipment.admin.modular.infromation.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectTargetSetEnvironment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xss
 * @since 2018-08-20
 */
public interface ProjectTargetSetEnvironmentMapper extends BaseMapper<ProjectTargetSetEnvironment> {

    /**
     * 条件分页查询
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<ProjectTargetSetEnvironment> selectPageList(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO> wrapper);

    /**
     * 根据制造厂商和规格型号判重
     * @param request
     * @return
     */
    int checkBySpecificationAndManufactor(@Param("e") RequestDTO<ProjectTargetSetEnvironment> request);
}
