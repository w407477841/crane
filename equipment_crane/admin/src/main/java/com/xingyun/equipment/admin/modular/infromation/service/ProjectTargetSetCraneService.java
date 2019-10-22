package com.xingyun.equipment.admin.modular.infromation.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectTargetSetCrane;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xss
 * @since 2018-08-22
 */
@Service
public interface ProjectTargetSetCraneService extends IService<ProjectTargetSetCrane> {

    /**
     * 条件分页查询
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectTargetSetCrane>>> selectPageList(RequestDTO<ProjectTargetSetCrane> request);

    /**
     * 判重
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectTargetSetCrane>>> checkBySpecificationAndManufactor(RequestDTO<ProjectTargetSetCrane> request);

}
