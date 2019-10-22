package com.xywg.equipmentmonitor.modular.infromation.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectEnvironmentMonitorDataModel;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-08-24
 */
public interface IProjectEnvironmentMonitorDataModelService extends IService<ProjectEnvironmentMonitorDataModel> {
    /**
     * 新增
     * @param request
     * @return
     * @author yuanyang
     */
    ResultDTO<Object> insertModel(RequestDTO<ProjectEnvironmentMonitorDataModel> request);

    /**
     * 修改
     * @param request
     * @return
     * @author yuanyang
     */
    ResultDTO<Object> updateModel(RequestDTO<ProjectEnvironmentMonitorDataModel> request);

    /**
     * 列表
     * @param request
     * @return
     * @author yuanyang
     */
    ResultDTO<DataDTO<List<ProjectEnvironmentMonitorDataModel>>> selectPageList(RequestDTO<ProjectEnvironmentMonitorDataModel> request);
}
