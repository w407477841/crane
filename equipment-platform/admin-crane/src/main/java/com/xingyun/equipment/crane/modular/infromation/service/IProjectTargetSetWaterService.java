package com.xingyun.equipment.crane.modular.infromation.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.crane.modular.infromation.model.ProjectTargetSetWater;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2018-10-15
 */
public interface IProjectTargetSetWaterService extends IService<ProjectTargetSetWater> {
    /**
     * 获取列表
     * @param page
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<ProjectTargetSetWater> selectTargetSetWater(Page<ProjectTargetSetWater> page, RequestDTO<ProjectTargetSetWater> requestDTO) throws Exception;
}
