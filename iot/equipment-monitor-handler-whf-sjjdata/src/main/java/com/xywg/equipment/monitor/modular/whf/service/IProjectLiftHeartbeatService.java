package com.xywg.equipment.monitor.modular.whf.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftHeartbeat;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyifei
 * @since 2018-08-26
 */
public interface IProjectLiftHeartbeatService extends IService<ProjectLiftHeartbeat> {
    /**
     *  查询上一个 开机数据
     * @param deviceNo
     * @return
     */
    ProjectLiftHeartbeat preOpen( String deviceNo);
    /**
     *  查询上一个 运行数据
     * @param deviceNo
     * @return
     */
    ProjectLiftHeartbeat preLive( String deviceNo);

    /**
    * @author: wangyifei
    * Description:
    * Date: 18:11 2018/8/27
     * @param heartbeat  数据
    */
     void updateEndTime(ProjectLiftHeartbeat heartbeat);

     /**
     * @author: wangyifei
     * Description: 添加未受控数据，和一条受控数据
     * Date: 16:46 2018/8/28
      * @param heartbeat 数据
     */
     void doOpenBusiness(ProjectLiftHeartbeat heartbeat);

}
