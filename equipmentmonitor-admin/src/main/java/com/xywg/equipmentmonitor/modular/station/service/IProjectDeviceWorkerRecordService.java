package com.xywg.equipmentmonitor.modular.station.service;

import com.xywg.equipmentmonitor.modular.station.dto.BindWorkerDTO;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDeviceWorkerRecord;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2019-03-20
 */
public interface IProjectDeviceWorkerRecordService extends IService<ProjectDeviceWorkerRecord> {
    /**
     * 绑定工人
     * @param bindWorkerDTO
     */
    void bindWorker(BindWorkerDTO bindWorkerDTO);

}
