package com.xingyun.equipment.crane.modular.baseinfo.service;

import com.xingyun.equipment.crane.modular.baseinfo.model.ProjectMasterCetificateType;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-08-28
 */
public interface IProjectMasterCetificateTypeService extends IService<ProjectMasterCetificateType> {
    /**
     * 获取证书类型
     * @return
     * @throws Exception
     */
    List<ProjectMasterCetificateType> selectCetificateType() throws Exception;
}
