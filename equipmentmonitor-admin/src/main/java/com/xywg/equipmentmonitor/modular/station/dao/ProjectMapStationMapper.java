package com.xywg.equipmentmonitor.modular.station.dao;

import com.xywg.equipmentmonitor.modular.station.model.ProjectMapStation;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2019-03-20
 */
public interface ProjectMapStationMapper extends BaseMapper<ProjectMapStation> {
    /**
     *  根据地图Id删除
     * @param mapId
     */
    void deleteByMapId(@Param("mapId") Integer mapId);
}
