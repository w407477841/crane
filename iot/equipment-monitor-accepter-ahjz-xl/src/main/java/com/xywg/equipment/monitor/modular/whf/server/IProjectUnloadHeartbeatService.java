package com.xywg.equipment.monitor.modular.whf.server;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.whf.model.ProjectUnloadHeartbeat;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2019-07-11
 */
public interface IProjectUnloadHeartbeatService extends IService<ProjectUnloadHeartbeat> {
    void doOpenBusiness(ProjectUnloadHeartbeat heartbeat);
    void updateEndTime(ProjectUnloadHeartbeat heartbeat);
    /**
     *
     * @param deviceNo
     * @return
     */
    ProjectUnloadHeartbeat preOpen(String deviceNo);

}
