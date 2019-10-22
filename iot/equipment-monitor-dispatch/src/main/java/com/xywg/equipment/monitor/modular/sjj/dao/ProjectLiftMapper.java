package com.xywg.equipment.monitor.modular.sjj.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.sjj.model.ProjectLift;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qiyan
 * @since 2019-08-01
 */
@Mapper
public interface ProjectLiftMapper extends BaseMapper<ProjectLift> {

    /**
     * 获取需要转发的升降机设备号
     */
    List<String> selectDeviceNoOfNeedDispatch(@Param("dispatch") Integer dispatch);
}
