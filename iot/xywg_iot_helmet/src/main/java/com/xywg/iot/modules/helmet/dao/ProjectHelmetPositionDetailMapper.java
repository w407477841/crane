package com.xywg.iot.modules.helmet.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.iot.modules.helmet.model.ProjectHelmetPositionDetail;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
public interface ProjectHelmetPositionDetailMapper extends BaseMapper<ProjectHelmetPositionDetail> {

    void insertByMonth(@Param("tableName") String tableName, @Param("positionDetail")ProjectHelmetPositionDetail projectHelmetPositionDetail);
}
