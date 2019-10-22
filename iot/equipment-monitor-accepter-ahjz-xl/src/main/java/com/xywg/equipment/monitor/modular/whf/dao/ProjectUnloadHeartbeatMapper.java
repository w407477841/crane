package com.xywg.equipment.monitor.modular.whf.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectUnloadHeartbeat;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2019-07-11
 */
public interface ProjectUnloadHeartbeatMapper extends BaseMapper<ProjectUnloadHeartbeat> {
    @Select("SELECT id,create_time as createTime , end_time as endTime FROM `t_project_unload_heartbeat` where device_no = #{deviceNo} and status = 1 ORDER BY create_time DESC LIMIT 1")
    ProjectUnloadHeartbeat preOpen(@Param("deviceNo") String deviceNo);
}
