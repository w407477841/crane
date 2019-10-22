package com.xingyun.equipment.crane.modular.device.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectHelmetHeartbeat;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 * 安全帽在线离线状态切换履历表 Mapper 接口
 * </p>
 *
 * @author xss
 * @since 2018-12-05
 */
public interface ProjectHelmetHeartbeatMapper extends BaseMapper<ProjectHelmetHeartbeat> {

    /**
     * 运行数据
     * @param rowBounds
     * @param ew
     * @return
     */
    List<ProjectHelmetHeartbeat> selectMonitorStatus(RowBounds rowBounds, @Param("ew") EntityWrapper<RequestDTO> ew);
}
