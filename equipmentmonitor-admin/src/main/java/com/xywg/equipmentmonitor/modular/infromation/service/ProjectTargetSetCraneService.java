package com.xywg.equipmentmonitor.modular.infromation.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectTargetSetCrane;
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
