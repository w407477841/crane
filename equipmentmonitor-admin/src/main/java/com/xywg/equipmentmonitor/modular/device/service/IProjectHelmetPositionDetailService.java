package com.xywg.equipmentmonitor.modular.device.service;

import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetPositionDetail;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 安全帽定位明细(采集数据) 服务类
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
public interface IProjectHelmetPositionDetailService extends IService<ProjectHelmetPositionDetail> {
    /**
     * 最后位置
     * @param tableName
     * @param time
     * @param projectId
     * @return
     */
    List<ProjectHelmetPositionDetail> getLastLocation(String tableName, String time, Integer projectId);

    /**
     *  历史轨迹
     * @param tables
     * @param projectId
     * @param identityCode
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProjectHelmetPositionDetail> getLocations(List<String> tables,
                                                   Integer projectId,
                                                   String identityCode,
                                                  String beginTime,
                                                   String endTime);
}
