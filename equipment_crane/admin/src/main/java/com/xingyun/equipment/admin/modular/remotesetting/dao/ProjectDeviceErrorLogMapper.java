package com.xingyun.equipment.admin.modular.remotesetting.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.remotesetting.model.ProjectDeviceErrorLog;
import com.xingyun.equipment.admin.modular.remotesetting.model.ProjectDeviceUpgrade;
import com.xingyun.equipment.admin.modular.remotesetting.model.ProjectDeviceUpgradePackage;
import com.xingyun.equipment.admin.modular.remotesetting.vo.ProjectDeviceUpgradePackageVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xss
 * @since 2018-09-30
 */
public interface ProjectDeviceErrorLogMapper extends BaseMapper<ProjectDeviceErrorLog> {


}
