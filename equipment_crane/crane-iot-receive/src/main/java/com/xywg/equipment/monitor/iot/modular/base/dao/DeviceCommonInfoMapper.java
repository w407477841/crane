package com.xywg.equipment.monitor.iot.modular.base.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hjy
 * @since 2018-08-20
 * 用于修改不同表的相同属性.传入不同的表名参数,字段参数
 */
@Mapper
public interface DeviceCommonInfoMapper {

    /**
     * 更新设备状态
     *
     * @param tableName
     * @param state
     */
    void updateDeviceIsOnLineState(@Param("tableName") String tableName, @Param("state") Integer state,
                                   @Param("deviceNo") String deviceNo);

}
