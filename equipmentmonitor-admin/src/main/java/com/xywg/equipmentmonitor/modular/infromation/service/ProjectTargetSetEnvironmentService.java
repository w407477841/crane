package com.xywg.equipmentmonitor.modular.infromation.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectTargetSetEnvironment;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xss
 * @since 2018-08-20
 */
public interface ProjectTargetSetEnvironmentService extends IService<ProjectTargetSetEnvironment> {

    /**
     * 条件分页查询
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectTargetSetEnvironment>>> getPageList(RequestDTO<ProjectTargetSetEnvironment> request);

    /**
     * 根据制造厂商和规格型号判重
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectTargetSetEnvironment>>> checkBySpecificationAndManufactor(RequestDTO<ProjectTargetSetEnvironment> request);
}
