package com.xingyun.equipment.admin.modular.infromation.service;

import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectLiftDataModel;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-08-24
 */
public interface IProjectLiftDataModelService extends IService<ProjectLiftDataModel> {
    /**
     * 新增
     * @param request
     * @return
     * @author yuanyang
     */
    ResultDTO<Object> insertModel(RequestDTO<ProjectLiftDataModel> request);

    /**
     * 修改
     * @param request
     * @return
     * @author yuanyang
     */
    ResultDTO<Object> updateModel(RequestDTO<ProjectLiftDataModel> request);

    /**
     * 分页查询
     * @param request
     * @return
     * @author yuanyang
     */
    ResultDTO<DataDTO<List<ProjectLiftDataModel>>> selectPageList(RequestDTO<ProjectLiftDataModel> request);
}
