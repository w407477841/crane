package com.xywg.equipmentmonitor.modular.remotesetting.dao;

import com.xywg.equipmentmonitor.modular.remotesetting.model.DeviceLogInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjy
 * @since 2018-09-30
 */
public interface DeviceLogManagementMapper{
    /**
     * 条件分页查询设备信息
     * @param rowBounds
     * @param deviceLogInfo
     * @return
     */
    List<DeviceLogInfo> getPageList(RowBounds rowBounds,@Param("deviceLogInfo")DeviceLogInfo deviceLogInfo);

}
