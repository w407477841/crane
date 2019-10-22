package com.xingyun.equipment.crane.modular.remotesetting.service;
import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectDeviceRestartRecord;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectDeviceUpgrade;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectDeviceUpgradePackage;
import com.xingyun.equipment.crane.modular.remotesetting.vo.ProjectDeviceUpgradePackageVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  设备重启
 * </p>
 *
 * @author hjy
 * @since 2018-09-30
 */
@Service
public interface ProjectDeviceRestartRecordService extends IService<ProjectDeviceRestartRecord>{

   /**
     * 条件分页查询
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectDeviceRestartRecord>>> getPageList(RequestDTO<ProjectDeviceRestartRecord> request);



}
