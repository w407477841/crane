package com.xingyun.equipment.crane.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.dto.ProjectTransfersDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectHelmet;

import java.util.List;

/**
 * <p>
 * 安全帽 服务类
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
public interface IProjectHelmetService extends IService<ProjectHelmet> {

    /**
     * 一览
     *
     * @param res
     * @return
     */
    ResultDTO<DataDTO<List<ProjectHelmet>>> selectPageList(RequestDTO<ProjectHelmet> res);


    /**
     * 安全帽启用
     */
    ResultDTO<Object> updateStatus(RequestDTO<ProjectHelmet> res);

    /**
     * 项目调拨
     * @param res
     * @return
     */
    ResultDTO<Object> projectTransfers(RequestDTO<ProjectTransfersDTO> res);

    /**
     * 人员调拨
     * @param res
     * @return
     */
    ResultDTO<Object> personTransfers(RequestDTO<ProjectHelmet> res);

    /**
     * 更新或插入数据
     * @param res
     * @return
     */
    ResultDTO<Object> updateOrInsertProjectHelmet(RequestDTO<ProjectHelmet> res);

}
