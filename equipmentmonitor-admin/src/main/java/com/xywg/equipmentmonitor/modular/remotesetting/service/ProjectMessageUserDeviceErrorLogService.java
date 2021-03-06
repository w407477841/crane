package com.xywg.equipmentmonitor.modular.remotesetting.service;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectDeviceUpgrade;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectDeviceUpgradePackage;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectMessageUserDeviceErrorLog;
import com.xywg.equipmentmonitor.modular.remotesetting.vo.ProjectDeviceUpgradePackageVO;
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
public interface ProjectMessageUserDeviceErrorLogService extends IService<ProjectMessageUserDeviceErrorLog>{

    /**
     * 条件分页查询
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectMessageUserDeviceErrorLog>>> getPageList(RequestDTO request);

    /**
     * 新增
     * @param list
     * @return
     *//*
    ResultDTO<DataDTO<List<ProjectMessageUserDeviceErrorLog>>> insertInfo(List<ProjectDeviceUpgradePackage> list);*/


}
