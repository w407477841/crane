package com.xingyun.equipment.crane.modular.infromation.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.infromation.model.ProjectCraneDataModel;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-08-24
 */
public interface IProjectCraneDataModelService extends IService<ProjectCraneDataModel> {
    /**
     * 新增
     * @param request
     * @return
     * @author yuanyang
     */
    ResultDTO<Object> insertModel(RequestDTO<ProjectCraneDataModel> request);

    /**
     * 修改
     * @param request
     * @return
     * @author yuanyang
     */
    ResultDTO<Object> updateModel(RequestDTO<ProjectCraneDataModel> request);

    /**
     * 列表
     * @param request
     * @return
     * @author yuanyang
     */
    ResultDTO<DataDTO<List<ProjectCraneDataModel>>> selectPageList(RequestDTO<ProjectCraneDataModel> request);
}
