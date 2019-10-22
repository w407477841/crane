package com.xywg.equipment.monitor.iot.modular.crane.service;

import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneHeartbeat;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-08-26
 */
public interface IProjectCraneHeartbeatService extends IService<ProjectCraneHeartbeat> {
    /**
     *  查询上一个 开机数据
     * @param deviceNo
     * @return
     */

    ProjectCraneHeartbeat preOpen( String deviceNo);

    /**
     *  查询上一个 运行数据
     * @param deviceNo
     * @return
     */

    ProjectCraneHeartbeat preLive(String deviceNo);

    /**
    * @author: wangyifei
    * Description 更新关闭时间
    * Date: 18:21 2018/8/27
     * @param heartbeat  数据
    */
    void updateEndTime(ProjectCraneHeartbeat heartbeat);


    /**
     * @author: wangyifei
     * Description: 添加未受控数据，和一条受控数据
     * Date: 16:46 2018/8/28
     * @param heartbeat 数据
     */
    void doOpenBusiness(ProjectCraneHeartbeat heartbeat);
}
