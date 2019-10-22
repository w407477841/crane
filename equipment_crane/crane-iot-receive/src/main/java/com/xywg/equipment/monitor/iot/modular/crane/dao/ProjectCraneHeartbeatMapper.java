package com.xywg.equipment.monitor.iot.modular.crane.dao;

import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneHeartbeat;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-08-26
 */
public interface ProjectCraneHeartbeatMapper extends BaseMapper<ProjectCraneHeartbeat> {
    /**
     *  查询上一个 开机数据
     * @param deviceNo
     * @return
     */

    @Select("SELECT id,create_time as createTime ,end_time as endTime FROM `t_project_crane_heartbeat` where device_no = #{deviceNo} and status = 1 ORDER BY create_time DESC LIMIT 1")
    ProjectCraneHeartbeat preOpen(@Param("deviceNo") String deviceNo);

    @Select("SELECT id,create_time as createTime ,end_time as endTime FROM `t_project_crane_heartbeat` where device_no = #{deviceNo} and status = 0 ORDER BY create_time DESC LIMIT 1")
    ProjectCraneHeartbeat preClose(@Param("deviceNo") String deviceNo);

    /**
     *  查询上一个 运行数据
     * @param deviceNo
     * @return
     */

    @Select("SELECT id,create_time as createTime ,end_time as endTime FROM `t_project_crane_heartbeat` where device_no = #{deviceNo} ORDER BY create_time DESC LIMIT 1")
    ProjectCraneHeartbeat preLive(@Param("deviceNo") String deviceNo);
}
