package com.xywg.equipmentmonitor.modular.remotesetting.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectDeviceUpgrade;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectMessageUserDeviceErrorLog;
import com.xywg.equipmentmonitor.modular.remotesetting.vo.ProjectDeviceUpgradePackageVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 *
 *  设备日志用户Mapper 接口
 */
public interface ProjectMessageUserDeviceErrorLogMapper extends BaseMapper<ProjectMessageUserDeviceErrorLog> {

    /**
     * 条件分页查询
     * @param rowBounds
     * @return
     */
    List<ProjectMessageUserDeviceErrorLog> getPageList(RowBounds rowBounds);


}
