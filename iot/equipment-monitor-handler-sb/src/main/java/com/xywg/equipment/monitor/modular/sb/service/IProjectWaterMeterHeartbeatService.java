package com.xywg.equipment.monitor.modular.sb.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.sb.model.ProjectWaterMeterHeartbeat;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-09-17
 */
public interface IProjectWaterMeterHeartbeatService extends IService<ProjectWaterMeterHeartbeat> {
    /**
     * 获取最新一条心跳数据
     * @param eleId
     * @return
     */
     ProjectWaterMeterHeartbeat getLastInfo(int eleId);



    ProjectWaterMeterHeartbeat preOpen( Integer electricId);

    /**
     *  查询上一个 运行数据
     * @return
     */

    ProjectWaterMeterHeartbeat preLive(Integer electricId);

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
