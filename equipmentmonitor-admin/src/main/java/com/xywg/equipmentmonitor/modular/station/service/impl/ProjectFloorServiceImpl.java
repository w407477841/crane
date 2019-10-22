package com.xywg.equipmentmonitor.modular.station.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.modular.station.dao.ProjectFloorMapper;
import com.xywg.equipmentmonitor.modular.station.model.ProjectFloor;
import com.xywg.equipmentmonitor.modular.station.service.IProjectFloorService;
import com.xywg.equipmentmonitor.modular.station.vo.FloorVO;
import com.xywg.equipmentmonitor.modular.station.vo.LouVO;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2019-03-22
 */
@Service
public class ProjectFloorServiceImpl extends ServiceImpl<ProjectFloorMapper, ProjectFloor> implements IProjectFloorService {

    @Override
    public void packageFloors(List<ProjectFloor> lists, List<LouVO> louhao) {

        lists.forEach(item -> {
            if (item.getPid().equals(new Integer(0))) {
                louhao.add(new LouVO(item.getId(), item.getName(),item.getStatus()));
            }
        });
        for (int i = 0; i < louhao.size(); i++) {
            LouVO louVO = louhao.get(i);
            List<FloorVO> floorVOS = new LinkedList();
            FloorVO floorVO;
            for (ProjectFloor projectFloor : lists) {
                if (louVO.getId().equals(projectFloor.getPid())) {
                    floorVO = new FloorVO(projectFloor.getId(), projectFloor.getName(),projectFloor.getStatus());
                    floorVOS.add(floorVO);
                }
            }
            louhao.get(i).setFloors(floorVOS);
        }
    }
}