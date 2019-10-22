package com.xywg.equipment.monitor.modular.yc.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.yc.model.ProjectEnvironmentMonitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qiyan
 * @since 2019-08-01
 */
@Mapper
public interface ProjectEnvironmentMonitorMapper extends BaseMapper<ProjectEnvironmentMonitor> {

    /**
     * 获取需要转发的扬尘设备号
     */
    List<String> selectDeviceNoOfNeedDispatch(@Param("dispatch") Integer dispatch);
}
