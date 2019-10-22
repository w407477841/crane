package com.xingyun.equipment.crane.modular.remotesetting.service;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.remotesetting.model.DeviceLogInfo;
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
public interface DeviceLogManagementService{

    /**
     * 条件分页查询
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<DeviceLogInfo>>> getPageList(RequestDTO<DeviceLogInfo> request);

    /**
     * 设备重启
     * @param request
     * @return
     */
    ResultDTO<Object> deviceReboot(RequestDTO<List<DeviceLogInfo>> request);

}
