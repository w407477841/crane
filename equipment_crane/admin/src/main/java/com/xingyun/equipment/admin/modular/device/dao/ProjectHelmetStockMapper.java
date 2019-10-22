package com.xingyun.equipment.admin.modular.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectHelmetStock;
import com.xingyun.equipment.admin.modular.device.vo.DeviceOutStockVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 * 安全帽 Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
public interface ProjectHelmetStockMapper extends BaseMapper<ProjectHelmetStock> {
    /**
     * 分页查询
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<ProjectHelmetStock> selectPageList(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO<ProjectHelmetStock>> wrapper);

    /**
     * 根据imei号 修改状态
     * @param status
     * @param imeis
     */
    void updateStatus(@Param("status")Integer status,@Param("imeis")List<String> imeis,
                      @Param("modifyUser")Integer modifyUser,@Param("type")Integer type);


    void insertDeviceData(@Param("tableName")String tableName, @Param("list")List<DeviceOutStockVo> list);
}
