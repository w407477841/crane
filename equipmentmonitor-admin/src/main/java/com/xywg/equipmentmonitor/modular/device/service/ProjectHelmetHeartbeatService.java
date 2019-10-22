package com.xywg.equipmentmonitor.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetHeartbeat;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 安全帽在线离线状态切换履历表 服务类
 * </p>
 *
 * @author xss
 * @since 2018-12-05
 */
@Service
public interface ProjectHelmetHeartbeatService extends IService<ProjectHelmetHeartbeat> {

    /**
     * 运行数据
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectHelmetHeartbeat>>> getList(RequestDTO request);
}
