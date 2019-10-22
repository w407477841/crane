package com.xingyun.equipment.crane.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectHelmet;
import com.xingyun.equipment.crane.modular.device.model.ProjectHelmetStock;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 安全帽 服务类
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
public interface ProjectHelmetStockService extends IService<ProjectHelmetStock> {

    /**
     * 一览
     *
     * @param res
     * @return
     */
    ResultDTO<DataDTO<List<ProjectHelmetStock>>> selectPageList(RequestDTO<ProjectHelmetStock> res);

    /**
     * 出库
     * @param res
     * @return
     */
    ResultDTO<Object> stockOut(RequestDTO<ProjectHelmetStock> res);


    /**
     * 文件批量入库
     * @param multipartFile
     * @return
     */
    ResultDTO<Object> fileUpload(MultipartFile multipartFile);
    //ResultDTO<Object> updateOrInsertProjectHelmet(RequestDTO<ProjectHelmet> res);
}
