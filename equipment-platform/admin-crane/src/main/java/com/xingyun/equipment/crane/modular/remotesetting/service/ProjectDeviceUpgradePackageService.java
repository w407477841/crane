package com.xingyun.equipment.crane.modular.remotesetting.service;
import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectDeviceUpgrade;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectDeviceUpgradePackage;
import com.xingyun.equipment.crane.modular.remotesetting.vo.ProjectDeviceUpgradePackageVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xss
 * @since 2018-09-30
 */
@Service
public interface ProjectDeviceUpgradePackageService extends IService<ProjectDeviceUpgradePackage>{

    /**
     * 条件分页查询
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectDeviceUpgradePackageVO>>> getPageList(RequestDTO request);

    /**
     * 新增
     * @param list
     * @return
     */
    ResultDTO<DataDTO<List<ProjectDeviceUpgradePackage>>> insertInfo(List<ProjectDeviceUpgradePackage> list);
    /**
     * 条件分页查询设备列表
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectDeviceUpgrade>>> getDeviceList(RequestDTO request);

    /**
     * 添加升级信息 并通知设备
     * @param id
     * @param ids
     * @return
     */
    ResultDTO<Object>  insertDeviceUpgrade(Integer id, String ids);

}
