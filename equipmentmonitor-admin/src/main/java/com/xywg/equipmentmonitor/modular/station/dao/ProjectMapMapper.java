package com.xywg.equipmentmonitor.modular.station.dao;

import com.xywg.equipmentmonitor.modular.station.model.ProjectMap;
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
public interface ProjectMapMapper extends BaseMapper<ProjectMap> {
    /**
     *  根据项目删除
     * @param projectId
     */
    void deleteByProject(@Param("projectId") Integer projectId);


}
