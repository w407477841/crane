package com.xywg.iot.modular.station.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.modular.station.model.ProjectDeviceWorkerRecord;
import com.xywg.iot.modular.station.dao.ProjectDeviceWorkerRecordMapper;
import com.xywg.iot.modular.station.service.IProjectDeviceWorkerRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2019-03-21
 */
@Service
public class ProjectDeviceWorkerRecordServiceImpl extends ServiceImpl<ProjectDeviceWorkerRecordMapper, ProjectDeviceWorkerRecord> implements IProjectDeviceWorkerRecordService {

    @Override
    public ProjectDeviceWorkerRecord selectByTagId(Integer id, String tagNo,Integer projectId) {

        Wrapper<ProjectDeviceWorkerRecord> wrapper =new EntityWrapper<>();
        wrapper.eq("device_id",id);
        wrapper.eq("device_no",tagNo);
        wrapper.eq("project_id",projectId);

        return this.selectOne(wrapper);

    }
}
