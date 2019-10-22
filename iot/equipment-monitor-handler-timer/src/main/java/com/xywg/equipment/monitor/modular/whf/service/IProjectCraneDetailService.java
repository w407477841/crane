package com.xywg.equipment.monitor.modular.whf.service;


import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.whf.model.ProjectCraneDetail;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
public interface IProjectCraneDetailService extends IService<ProjectCraneDetail> {
    /**
     * 新增详细数据
     * @param tableName 表名
     * @param projectCraneDetail 新增数据
     * */
    void createDetail(@Param("t") ProjectCraneDetail projectCraneDetail, @Param("tableName") String tableName);

}
