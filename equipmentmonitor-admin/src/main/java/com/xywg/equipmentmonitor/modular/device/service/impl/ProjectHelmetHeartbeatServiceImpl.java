package com.xywg.equipmentmonitor.modular.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectHelmetHeartbeatMapper;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetHeartbeat;
import com.xywg.equipmentmonitor.modular.device.service.IProjectHelmetService;
import com.xywg.equipmentmonitor.modular.device.service.ProjectHelmetHeartbeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 安全帽在线离线状态切换履历表 服务实现类
 * </p>
 *
 * @author xss
 * @since 2018-12-05
 */
@Service
public class ProjectHelmetHeartbeatServiceImpl extends ServiceImpl<ProjectHelmetHeartbeatMapper, ProjectHelmetHeartbeat> implements ProjectHelmetHeartbeatService {

    @Override
    public ResultDTO<DataDTO<List<ProjectHelmetHeartbeat>>> getList(RequestDTO request) {
        EntityWrapper<RequestDTO> ew = new EntityWrapper<RequestDTO>();
        Page<ProjectHelmetHeartbeat> page=new Page<>(request.getPageNum(),request.getPageSize());
        ew.eq("helmet_id",request.getId());
        if(request.getEndDate()!=null && request.getEndDate()!=""){
            ew.lt("end_time",request.getEndDate().substring(0,10)+" 00:00:00");
        }
        if(request.getBeginDate()!=null && request.getBeginDate()!=""){
            ew.gt("create_time",request.getBeginDate().substring(0,10)+" 00:00:00");
        }
        List<ProjectHelmetHeartbeat> list=baseMapper.selectMonitorStatus(page,ew);
        //便利一遍 如果没有结束时间就设为当前时间
        for(ProjectHelmetHeartbeat h :list){
            if(h.getEndTime()==null){
                h.setEndTime(new Date());
            }
        }
        return new ResultDTO<>(true,DataDTO.factory(list,list.size()));
    }
}
