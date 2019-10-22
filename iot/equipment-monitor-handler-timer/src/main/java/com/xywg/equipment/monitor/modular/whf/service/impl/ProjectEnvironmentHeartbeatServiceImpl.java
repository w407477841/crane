package com.xywg.equipment.monitor.modular.whf.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectEnvironmentHeartbeatMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentHeartbeat;
import com.xywg.equipment.monitor.modular.whf.service.IProjectEnvironmentHeartbeatService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-08-26
 */
@Service
public class ProjectEnvironmentHeartbeatServiceImpl extends ServiceImpl<ProjectEnvironmentHeartbeatMapper, ProjectEnvironmentHeartbeat> implements IProjectEnvironmentHeartbeatService {

    @Override
    public ProjectEnvironmentHeartbeat preOpen(String deviceNo) {
        return baseMapper.preOpen(deviceNo);
    }

    @Override
    public ProjectEnvironmentHeartbeat preLive(String deviceNo) {
        return baseMapper.preLive(deviceNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEndTime(ProjectEnvironmentHeartbeat heartbeat) {
        ProjectEnvironmentHeartbeat preOpen =	preOpen(heartbeat.getDeviceNo());

        if(preOpen != null && preOpen.getId()!=null){
            preOpen.setEndTime(heartbeat.getCreateTime());
            updateById(preOpen);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doOpenBusiness(ProjectEnvironmentHeartbeat heartbeat) {

        //查上一条 受控数据
        ProjectEnvironmentHeartbeat preOpen= preOpen(heartbeat.getDeviceNo());
        if(preOpen ==null){
            //没有受控数据，说明是第一次采集
            //不需要 添加未受控数据
        }else{
            ProjectEnvironmentHeartbeat uncontrol = new ProjectEnvironmentHeartbeat();
            BeanUtils.copyProperties(heartbeat,uncontrol);
            uncontrol.setStatus(0);
            uncontrol.setCreateTime(preOpen.getEndTime());
            uncontrol.setEndTime(heartbeat.getCreateTime());
            this.insert(uncontrol);

        }

        heartbeat.setStatus(1);
        heartbeat.setEndTime(heartbeat.getCreateTime());
        this.insert(heartbeat);

    }
}
