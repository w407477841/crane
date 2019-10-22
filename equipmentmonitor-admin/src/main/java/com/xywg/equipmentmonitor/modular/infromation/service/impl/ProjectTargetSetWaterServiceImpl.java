package com.xywg.equipmentmonitor.modular.infromation.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.infromation.model.ProjectTargetSetWater;
import com.xywg.equipmentmonitor.modular.infromation.dao.ProjectTargetSetWaterMapper;
import com.xywg.equipmentmonitor.modular.infromation.service.IProjectTargetSetWaterService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-10-15
 */
@Service
public class ProjectTargetSetWaterServiceImpl extends ServiceImpl<ProjectTargetSetWaterMapper, ProjectTargetSetWater> implements IProjectTargetSetWaterService {

    @Override
    public List<ProjectTargetSetWater> selectTargetSetWater(Page<ProjectTargetSetWater> page, RequestDTO<ProjectTargetSetWater> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("orgIds",requestDTO.getOrgIds());
        map.put("key",requestDTO.getKey());
        return baseMapper.selectTargetSetWater(page,map);
    }
}
