package com.xywg.equipmentmonitor.modular.station.service;

import com.xywg.equipmentmonitor.modular.station.model.ProjectFloor;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.modular.station.vo.LouVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2019-03-22
 */
public interface IProjectFloorService extends IService<ProjectFloor> {

    /**
     *
     * @param lists
     * @param louhao
     */
    void packageFloors(List<ProjectFloor> lists, List<LouVO> louhao);


}
