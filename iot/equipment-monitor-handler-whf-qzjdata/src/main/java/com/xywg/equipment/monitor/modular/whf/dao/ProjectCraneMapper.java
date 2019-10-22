package com.xywg.equipment.monitor.modular.whf.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectCrane;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
public interface ProjectCraneMapper extends BaseMapper<ProjectCrane> {
    /**
     *
     * @return 所有设备
     */
       List<Map<String,String>> selectAllDevice();

    /**
     *
     * @param tableName
     * @param deviceNo
     * @param status
     */
    void doDeviceOnlineChange(@Param("tableName") String tableName,@Param("deviceNo") String deviceNo,@Param("status") int status);

}
