package com.xywg.equipment.monitor.iot.modular.crane.service.impl;

import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneHeartbeat;
import com.xywg.equipment.monitor.iot.modular.crane.dao.ProjectCraneHeartbeatMapper;
import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneHeartbeatService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
        ProjectCraneHeartbeat preClose = preClose(heartbeat.getDeviceNo());
        if(preClose!=null&&preClose.getEndTime()==null){
            // 设备打开多个连接未及时关闭
        }else{
            if(preOpen !=null && preOpen.getId() !=null){
                preOpen.setEndTime(heartbeat.getCreateTime());
                updateById(preOpen);
                ProjectCraneHeartbeat uncontrol = new ProjectCraneHeartbeat();
                BeanUtils.copyProperties(heartbeat,uncontrol);
                uncontrol.setStatus(0);
                uncontrol.setEndTime(null);
                this.insert(uncontrol);
            }else{
                ProjectCraneHeartbeat uncontrol = new ProjectCraneHeartbeat();
                BeanUtils.copyProperties(heartbeat,uncontrol);
                uncontrol.setStatus(0);
                uncontrol.setEndTime(null);
                this.insert(uncontrol);
            }
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
                // 存在最后一条未离线，修改结束时间
                preClose.setEndTime(heartbeat.getCreateTime());
                this.updateById(preClose);
                //添加一条在线数据
                ProjectCraneHeartbeat control = new ProjectCraneHeartbeat();
                BeanUtils.copyProperties(heartbeat,control);
                control.setStatus(1);
                control.setEndTime(null);
                this.insert(control);
            }else{
                ProjectCraneHeartbeat control = new ProjectCraneHeartbeat();
                BeanUtils.copyProperties(heartbeat,control);
                control.setStatus(1);
                control.setEndTime(null);
                this.insert(control);
            }
        }
    }
}
