package com.xywg.iot.modules.crane.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.modules.crane.dao.ProjectCraneHeartbeatMapper;
import com.xywg.iot.modules.crane.model.ProjectCraneHeartbeat;
import com.xywg.iot.modules.crane.service.IProjectCraneHeartbeatService;
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
public class ProjectCraneHeartbeatServiceImpl extends ServiceImpl<ProjectCraneHeartbeatMapper, ProjectCraneHeartbeat> implements IProjectCraneHeartbeatService {

    @Override
    public ProjectCraneHeartbeat preOpen(String deviceNo) {
        return baseMapper.preOpen(deviceNo);
    }

    @Override
    public ProjectCraneHeartbeat preLive(String deviceNo) {
        return baseMapper.preLive(deviceNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEndTime(ProjectCraneHeartbeat heartbeat) {
        ProjectCraneHeartbeat preOpen = preOpen(heartbeat.getDeviceNo());

        if(preOpen !=null && preOpen.getId() !=null){
            preOpen.setEndTime(heartbeat.getCreateTime());
            updateById(preOpen);
        }

    }

    @Override
    public void doOpenBusiness(ProjectCraneHeartbeat heartbeat) {
        //查上一条 受控数据
        ProjectCraneHeartbeat preOpen= preOpen(heartbeat.getDeviceNo());
        if(preOpen ==null){
            //没有受控数据，说明是第一次采集
            //不需要 添加未受控数据
        }else{
            ProjectCraneHeartbeat uncontrol = new ProjectCraneHeartbeat();
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
