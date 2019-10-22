package com.xingyun.equipment.crane.modular.remotesetting.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectDeviceUpgrade;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectDeviceUpgradePackage;
import com.xingyun.equipment.crane.modular.remotesetting.vo.ProjectDeviceUpgradePackageVO;
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
public interface ProjectDeviceUpgradePackageMapper extends BaseMapper<ProjectDeviceUpgradePackage> {

    /**
     * 条件分页查询
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<ProjectDeviceUpgradePackageVO> getPageList(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO> wrapper);

    /**
     * 条件分页查询设备
     * @param rowBounds
     * @param mainTableName
     * @return
     */
    List<ProjectDeviceUpgrade> getDeviceList(RowBounds rowBounds, @Param("mainTableName") String mainTableName, @Param("subtablesName") String subtablesName, @Param("keyWord") String keyWord);

    /**
     * 条件分页查询设备
     * @param mainTableName
     * @return
     */
    List<ProjectDeviceUpgrade> getDeviceListNoPage(@Param("mainTableName") String mainTableName, @Param("subtablesName") String subtablesName, @Param("ids") String[] ids);


    /**
     * 相关升级履历
     * @param subtablesName
     * @param list
     */
    void  insertUpgradeRecord(@Param("subtablesName") String subtablesName
            , @Param("list") List<ProjectDeviceUpgrade> list);

}
