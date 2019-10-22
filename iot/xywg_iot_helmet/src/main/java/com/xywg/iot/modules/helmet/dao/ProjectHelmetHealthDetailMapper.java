package com.xywg.iot.modules.helmet.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.iot.modules.helmet.model.ProjectHelmetHealthDetail;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
public interface ProjectHelmetHealthDetailMapper extends BaseMapper<ProjectHelmetHealthDetail> {

    void insertByMonth(ProjectHelmetHealthDetail healthDetail);
}
