package com.xywg.equipment.monitor.iot.modular.envmon.service;

import com.xywg.equipment.monitor.iot.modular.envmon.model.ProjectEnvironmentHeartbeat;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyifei
 * @since 2018-08-26
 */
public interface IProjectEnvironmentHeartbeatService extends IService<ProjectEnvironmentHeartbeat> {

    /**
     *
     * @param deviceNo
     * @return
     */
    ProjectEnvironmentHeartbeat preOpen( String deviceNo);

    /**
     *
     * @param deviceNo
     * @return
     */
    ProjectEnvironmentHeartbeat preLive(String deviceNo);

    /**
    * @author: wangyifei
    * Description:
    * Date: 18:34 2018/8/27
     * @param heartbeat 数据
    */
    void updateEndTime(ProjectEnvironmentHeartbeat heartbeat);

    /**
     * @author: wangyifei
     * Description: 添加未受控数据，和一条受控数据
     * Date: 16:46 2018/8/28
     * @param heartbeat 数据
     */
    void doOpenBusiness(ProjectEnvironmentHeartbeat heartbeat);

}
