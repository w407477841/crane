package com.xingyun.equipment.timer.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.timer.dao.ProjectCraneHeartbeatMapper;
import com.xingyun.equipment.timer.model.ProjectCraneHeartbeat;
import com.xingyun.equipment.timer.service.IProjectCraneHeartbeatService;
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
 * @since 2018-08-26
 */
@Service
public class ProjectCraneHeartbeatServiceImpl extends ServiceImpl<ProjectCraneHeartbeatMapper, ProjectCraneHeartbeat> implements IProjectCraneHeartbeatService {

    @Override
    public ProjectCraneHeartbeat preOpen(String deviceNo) {
        return baseMapper.preOpen(deviceNo);
    }
    @Override
    public ProjectCraneHeartbeat preClose(String deviceNo) {
        return baseMapper.preClose(deviceNo);
    }
    @Override
    public ProjectCraneHeartbeat preLive(String deviceNo) {
        return baseMapper.preLive(deviceNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEndTime(ProjectCraneHeartbeat heartbeat) {
        // 更新在线记录的结束时间
        // 添加一条离线记录

        ProjectCraneHeartbeat preOpen = preOpen(heartbeat.getDeviceNo());

        if(preOpen !=null && preOpen.getId() !=null){
            preOpen.setEndTime(new Date());
            updateById(preOpen);
            ProjectCraneHeartbeat uncontrol = new ProjectCraneHeartbeat();
            BeanUtils.copyProperties(heartbeat,uncontrol);
            uncontrol.setStatus(0);
            uncontrol.setCreateTime(preOpen.getEndTime());
            uncontrol.setEndTime(null);
            this.insert(uncontrol);
        }else{
            ProjectCraneHeartbeat uncontrol = new ProjectCraneHeartbeat();
            BeanUtils.copyProperties(heartbeat,uncontrol);
            uncontrol.setStatus(0);
            uncontrol.setCreateTime(new Date());
            uncontrol.setEndTime(null);
            this.insert(uncontrol);
        }

    }

    @Override
    public void doOpenBusiness(ProjectCraneHeartbeat heartbeat) {
        ProjectCraneHeartbeat preOpen= preOpen(heartbeat.getDeviceNo());
        ProjectCraneHeartbeat preClose = preClose(heartbeat.getDeviceNo());
        if(preOpen !=null && preOpen.getEndTime()==null){
            //说明是 服务器关机
        }else{

            if(preClose!=null&&preClose.getId() !=null){
                preClose.setEndTime(new Date());
                this.updateById(preClose);
                ProjectCraneHeartbeat control = new ProjectCraneHeartbeat();
                BeanUtils.copyProperties(heartbeat,control);
                control.setStatus(1);
                control.setCreateTime(preOpen.getEndTime());
                control.setEndTime(null);
                this.insert(control);
            }else{
                ProjectCraneHeartbeat control = new ProjectCraneHeartbeat();
                BeanUtils.copyProperties(heartbeat,control);
                control.setStatus(1);
                control.setCreateTime(new Date());
                control.setEndTime(null);
                this.insert(control);
            }
        }
    }
}
