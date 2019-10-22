package com.xywg.iot.modular.station.service;

import com.xywg.iot.modular.station.model.ProjectDeviceWorkerRecord;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2019-03-21
 */
public interface IProjectDeviceWorkerRecordService extends IService<ProjectDeviceWorkerRecord> {
    /**
     *
      * @param id
     * @param tagNo
     * @return
     */
    ProjectDeviceWorkerRecord selectByTagId(Integer id,String tagNo,Integer projectId);

}
