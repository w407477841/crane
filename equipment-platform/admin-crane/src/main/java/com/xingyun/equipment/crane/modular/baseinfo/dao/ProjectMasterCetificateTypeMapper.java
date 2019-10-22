package com.xingyun.equipment.crane.modular.baseinfo.dao;

import com.xingyun.equipment.crane.modular.baseinfo.model.ProjectMasterCetificateType;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-08-28
 */
public interface ProjectMasterCetificateTypeMapper extends BaseMapper<ProjectMasterCetificateType> {
    /**
     * 获取证书类型
     * @return
     * @throws Exception
     */
    List<ProjectMasterCetificateType> selectCetificateType() throws Exception;
}
