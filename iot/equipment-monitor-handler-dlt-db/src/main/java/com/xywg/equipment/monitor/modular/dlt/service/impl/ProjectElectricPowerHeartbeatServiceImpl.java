package com.xywg.equipment.monitor.modular.dlt.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.dlt.dao.ProjectElectricPowerHeartbeatMapper;
import com.xywg.equipment.monitor.modular.dlt.model.ProjectElectricPowerHeartbeat;
import com.xywg.equipment.monitor.modular.dlt.service.IProjectElectricPowerHeartbeatService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
@Service
public class ProjectElectricPowerHeartbeatServiceImpl extends ServiceImpl<ProjectElectricPowerHeartbeatMapper, ProjectElectricPowerHeartbeat> implements IProjectElectricPowerHeartbeatService {
    @Override
    public ProjectElectricPowerHeartbeat getLastInfo(int eleId) {

        return baseMapper.getLastInfo(eleId);
    }

    @Override
    public ProjectElectricPowerHeartbeat preOpen(Integer electricId) {
        return baseMapper.preOpen(electricId);
    }

    @Override
    public ProjectElectricPowerHeartbeat preLive(Integer electricId) {
        return baseMapper.preLive(electricId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEndTime(Integer electricId, Date endTime) {
        ProjectElectricPowerHeartbeat preOpen =	preOpen(electricId);

        if(preOpen != null && preOpen.getId()!=null){
            preOpen.setEndTime(endTime);
            updateById(preOpen);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doOpenBusiness(Integer electricId,Date endTime) {

        //查上一条 受控数据
        ProjectElectricPowerHeartbeat preOpen =	preOpen(electricId);
        preOpen= preOpen(electricId);
        if(preOpen ==null){
            //没有受控数据，说明是第一次采集
            //不需要 添加未受控数据
            ProjectElectricPowerHeartbeat heartbeat =new ProjectElectricPowerHeartbeat();
            heartbeat.setStatus(1);
            heartbeat.setCreateTime(endTime);
            heartbeat.setEndTime(endTime);
            heartbeat.setElectricId(electricId);
            this.insert(heartbeat);

        }else{
            if(preOpen.getEndTime() == null){
                //说明上次是服务器关闭造成的,不需要重新 未受控和受控数据

            }else{
                ProjectElectricPowerHeartbeat uncontrol = new ProjectElectricPowerHeartbeat();
                uncontrol.setStatus(0);
                uncontrol.setElectricId(electricId);
                uncontrol.setCreateTime(preOpen.getEndTime());
                uncontrol.setEndTime(endTime);
                this.insert(uncontrol);

                ProjectElectricPowerHeartbeat heartbeat =new ProjectElectricPowerHeartbeat();
                heartbeat.setStatus(1);
                heartbeat.setCreateTime(endTime);
                heartbeat.setEndTime(endTime);
                heartbeat.setElectricId(electricId);
                this.insert(heartbeat);
            }



        }


    }
}
