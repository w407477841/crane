package com.xywg.equipment.monitor.modular.tj.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.modular.tj.model.ProjectCrane;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectCraneMapper extends BaseMapper<ProjectCrane> {

    /**
     * 获取需要转发的升降机设备号
     */
    List<String> selectDeviceNoOfNeedDispatch(@Param("dispatch") Integer dispatch);
}
