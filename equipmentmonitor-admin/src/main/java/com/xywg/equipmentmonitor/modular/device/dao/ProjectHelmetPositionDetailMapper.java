package com.xywg.equipmentmonitor.modular.device.dao;

import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetPositionDetail;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipmentmonitor.modular.station.vo.LastLocationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 安全帽定位明细(采集数据) Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
public interface ProjectHelmetPositionDetailMapper extends BaseMapper<ProjectHelmetPositionDetail> {

    /**
     *
     * @param tableName
     * @param time
     * @param projectId
     * @return
     */
    List<ProjectHelmetPositionDetail> getLastLocation(@Param("tableName") String tableName
                                                    ,@Param("time") String time,
                                                      @Param("projectId") Integer projectId);


    /**
     *  历史轨迹
     * @param tables
     * @param projectId
     * @param identityCode
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProjectHelmetPositionDetail> getLocations(@Param("tables")List<String> tables,
                                                   @Param("projectId") Integer projectId,
                                                   @Param("identityCode") String identityCode,
                                                    @Param("beginTime") String beginTime,
                                                   @Param("endTime") String endTime);

}
