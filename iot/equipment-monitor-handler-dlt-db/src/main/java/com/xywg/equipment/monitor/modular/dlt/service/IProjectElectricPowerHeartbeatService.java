package com.xywg.equipment.monitor.modular.dlt.service;


import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.dlt.model.ProjectElectricPowerHeartbeat;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
public interface IProjectElectricPowerHeartbeatService extends IService<ProjectElectricPowerHeartbeat> {
    /**
     * 获取最新一条心跳数据
     * @return
     */
    public ProjectElectricPowerHeartbeat getLastInfo(int eleId);




    ProjectElectricPowerHeartbeat preOpen( Integer electricId);

    /**
     *  查询上一个 运行数据
     * @return
     */

    ProjectElectricPowerHeartbeat preLive(Integer electricId);

    /**
     * @author: wangyifei
     * Description:
     * Date: 18:34 2018/8/27
     */
    void updateEndTime(Integer electricId, Date endTime);

    /**
     * @author: wangyifei
     * Description: 添加未受控数据，和一条受控数据
     * Date: 16:46 2018/8/28
     */
    void doOpenBusiness(Integer electricId,Date endTime);

}
