package com.xywg.equipment.monitor.iot.modular.lift.dao;

import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLiftDetail;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-22
 */
public interface ProjectLiftDetailMapper extends BaseMapper<ProjectLiftDetail> {
    /**
     * 添加详细数据
     * @param detail
     * @param tableName
     */
    void createDetail (@Param("t") ProjectLiftDetail detail,@Param("tableName") String tableName);

}
