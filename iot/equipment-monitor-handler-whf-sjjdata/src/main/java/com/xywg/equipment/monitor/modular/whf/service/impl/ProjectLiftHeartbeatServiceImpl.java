package com.xywg.equipment.monitor.modular.whf.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectLiftHeartbeatMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftHeartbeat;
import com.xywg.equipment.monitor.modular.whf.service.IProjectLiftHeartbeatService;
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
public class ProjectLiftHeartbeatServiceImpl extends ServiceImpl<ProjectLiftHeartbeatMapper, ProjectLiftHeartbeat> implements IProjectLiftHeartbeatService {

    @Override
    public ProjectLiftHeartbeat preOpen(String deviceNo) {
        return baseMapper.preOpen(deviceNo);
    }

    @Override
    public ProjectLiftHeartbeat preLive(String deviceNo) {
        return baseMapper.preLive(deviceNo);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEndTime(ProjectLiftHeartbeat heartbeat){

        ProjectLiftHeartbeat preOpen= preOpen(heartbeat.getDeviceNo());

        if(preOpen!=null&&preOpen.getId()!=null){
            preOpen.setEndTime(heartbeat.getCreateTime());
            updateById(preOpen);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doOpenBusiness(ProjectLiftHeartbeat heartbeat) {
        ProjectLiftHeartbeat preOpen= preOpen(heartbeat.getDeviceNo());
        if(preOpen ==null){
            //没有受控数据，说明是第一次采集
            //不需要 添加未受控数据
            heartbeat.setStatus(1);
            heartbeat.setEndTime(heartbeat.getCreateTime());
            this.insert(heartbeat);
        }else{

            if(preOpen.getEndTime()==null){

            }else{
                ProjectLiftHeartbeat uncontrol = new ProjectLiftHeartbeat();
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

}
