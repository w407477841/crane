package com.xywg.equipment.monitor.modular.whf.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftDetail;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-22
 */
public interface IProjectLiftDetailService extends IService<ProjectLiftDetail> {

    /**
     * 添加详细数据
     * @param detail
     * @param tableName
     */
    void createDetail(@Param("t") ProjectLiftDetail detail, @Param("tableName") String tableName);
}
