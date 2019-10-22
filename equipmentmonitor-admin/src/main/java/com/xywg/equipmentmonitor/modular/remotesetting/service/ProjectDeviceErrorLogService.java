package com.xywg.equipmentmonitor.modular.remotesetting.service;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectDeviceErrorLog;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectMessageDeviceError;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xss
 * @since 2018-09-30
 */
@Service
public interface ProjectDeviceErrorLogService extends IService<ProjectDeviceErrorLog>{
    /**
     * 条件分页查询
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectDeviceErrorLog>>> getPageList(RequestDTO<ProjectDeviceErrorLog> request);


}
