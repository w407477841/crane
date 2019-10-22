package com.xywg.equipmentmonitor.modular.station.service;

import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.station.dto.BindDTO;
import com.xywg.equipmentmonitor.modular.station.dto.ProjectMapDTO;
import com.xywg.equipmentmonitor.modular.station.model.ProjectMap;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2019-03-20
 */
public interface IProjectMapService extends IService<ProjectMap> {
    /**
     *  新增展示图 带逻辑的哟
     * @param mapDTO
     */
    void insertProjectMap(ProjectMapDTO mapDTO);

    /**
     * 绑定
     * @param bindDTO
     */
    ResultDTO bind(BindDTO bindDTO);

    /**
     *  删除地图
     * @param projectId
     */
    void deleteByProject(Integer projectId);

}
