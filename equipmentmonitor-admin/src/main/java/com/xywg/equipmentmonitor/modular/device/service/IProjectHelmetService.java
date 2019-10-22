package com.xywg.equipmentmonitor.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectTransfersDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmet;
import org.springframework.web.bind.annotation.RequestBody;

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
    ResultDTO<Object> personTransfers( RequestDTO<ProjectHelmet> res);

    /**
     * 更新或插入数据
     * @param res
     * @return
     */
    ResultDTO<Object> updateOrInsertProjectHelmet(RequestDTO<ProjectHelmet> res);

    /**
     * 微信绑定安全帽
     * @param res
     * @return
     */
    ResultDTO<Object> bindWeChat(RequestDTO<ProjectHelmet> res);
    /**
     * 更新
     * @param res
     * @return
     */
    ResultDTO<Object>  updateWechat(RequestDTO<ProjectHelmet> t);

}
