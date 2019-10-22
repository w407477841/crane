package com.xywg.equipment.monitor.modular.whf.server.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectUnloadHeartbeatMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectUnloadHeartbeat;
import com.xywg.equipment.monitor.modular.whf.server.IProjectUnloadHeartbeatService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2019-07-11
 */
@Service
public class ProjectUnloadHeartbeatServiceImpl extends ServiceImpl<ProjectUnloadHeartbeatMapper, ProjectUnloadHeartbeat> implements IProjectUnloadHeartbeatService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEndTime(ProjectUnloadHeartbeat heartbeat) {
        ProjectUnloadHeartbeat preOpen =	preOpen(heartbeat.getDeviceNo());

        if(preOpen != null && preOpen.getId()!=null){
            preOpen.setEndTime(heartbeat.getCreateTime());
            updateById(preOpen);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doOpenBusiness(ProjectUnloadHeartbeat heartbeat) {

        //查上一条 受控数据
        ProjectUnloadHeartbeat preOpen= preOpen(heartbeat.getDeviceNo());
        if(preOpen ==null){
            //没有受控数据，说明是第一次采集
            heartbeat.setStatus(1);
            heartbeat.setEndTime(null);
            this.insert(heartbeat);

        }else{
            if(preOpen.getEndTime() ==null){
            }else{
                ProjectUnloadHeartbeat uncontrol = new ProjectUnloadHeartbeat();
                BeanUtils.copyProperties(heartbeat,uncontrol);
                uncontrol.setStatus(0);
                uncontrol.setCreateTime(preOpen.getEndTime());
                uncontrol.setEndTime(heartbeat.getCreateTime());
                this.insert(uncontrol);
                heartbeat.setStatus(1);
                heartbeat.setEndTime(null);
                this.insert(heartbeat);
            }


        }

    }
    @Override
    public ProjectUnloadHeartbeat preOpen(String deviceNo) {
        return baseMapper.preOpen(deviceNo);
    }


}
