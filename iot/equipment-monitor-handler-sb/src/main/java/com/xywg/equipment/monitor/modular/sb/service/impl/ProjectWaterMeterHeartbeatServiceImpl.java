package com.xywg.equipment.monitor.modular.sb.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.sb.dao.ProjectWaterMeterHeartbeatMapper;
import com.xywg.equipment.monitor.modular.sb.model.ProjectWaterMeterHeartbeat;
import com.xywg.equipment.monitor.modular.sb.service.IProjectWaterMeterHeartbeatService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-09-17
 */
@Service
public class ProjectWaterMeterHeartbeatServiceImpl extends ServiceImpl<ProjectWaterMeterHeartbeatMapper, ProjectWaterMeterHeartbeat> implements IProjectWaterMeterHeartbeatService {
    @Override
    public ProjectWaterMeterHeartbeat getLastInfo(int eleId) {

        return baseMapper.getLastInfo(eleId);
    }

    @Override
    public ProjectWaterMeterHeartbeat preOpen(Integer electricId) {
        return baseMapper.preOpen(electricId);
    }

    @Override
    public ProjectWaterMeterHeartbeat preLive(Integer electricId) {
        return baseMapper.preLive(electricId);
    }

    @Override
    public void updateEndTime(Integer electricId, Date endTime) {
        ProjectWaterMeterHeartbeat preOpen =	preOpen(electricId);

        if(preOpen != null && preOpen.getId()!=null){
            preOpen.setEndTime(endTime);
            updateById(preOpen);
        }
    }

    @Override
    public void doOpenBusiness(Integer electricId, Date endTime) {
        //查上一条 受控数据
        ProjectWaterMeterHeartbeat preOpen =	preOpen(electricId);
        preOpen= preOpen(electricId);
        if(preOpen ==null){
            //没有受控数据，说明是第一次采集
            //不需要 添加未受控数据
            ProjectWaterMeterHeartbeat heartbeat =new ProjectWaterMeterHeartbeat();
            heartbeat.setStatus(1);
            heartbeat.setCreateTime(endTime);
            heartbeat.setEndTime(null);
            heartbeat.setElectricId(electricId);
            this.insert(heartbeat);
        }else{
            if(preOpen.getEndTime() == null){
                //说明上次是服务器关闭造成的,不需要重新 未受控和受控数据

            }else{
                //添加未受控
                ProjectWaterMeterHeartbeat uncontrol = new ProjectWaterMeterHeartbeat();
                uncontrol.setStatus(0);
                uncontrol.setElectricId(electricId);
                uncontrol.setCreateTime(preOpen.getEndTime());
                uncontrol.setEndTime(endTime);
                this.insert(uncontrol);
                //添加受控
                ProjectWaterMeterHeartbeat heartbeat =new ProjectWaterMeterHeartbeat();
                heartbeat.setStatus(1);
                heartbeat.setCreateTime(endTime);
                heartbeat.setEndTime(null);
                heartbeat.setElectricId(electricId);
                this.insert(heartbeat);

            }
        }

    }
}
