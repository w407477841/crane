package com.xywg.equipmentmonitor.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentHeartbeat;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjy
 * @since 2018-08-28
 */
@Service
public interface ProjectEnvironmentMonitorHeartbeatService extends IService<ProjectEnvironmentHeartbeat>{

    /**
     * 监控状态列表
     * @param res
     * @return
     */
    ResultDTO<DataDTO<List<ProjectEnvironmentHeartbeat>>> selectPageList(RequestDTO<ProjectEnvironmentHeartbeat> res);

}
