package com.xywg.equipment.monitor.modular.whf.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitorDetail;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-21
 */
public interface IProjectEnvironmentMonitorDetailService extends IService<ProjectEnvironmentMonitorDetail> {
    /**
     * @param detail
     * @param tableName
     */
    void createDetail( ProjectEnvironmentMonitorDetail detail,String tableName) throws  Exception;

}
