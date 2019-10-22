package com.xywg.equipment.monitor.iot.modular.spray.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSpray;
import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSprayBind;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjy
 * @since 2018-08-20
 */
public interface ProjectSprayBindMapper extends BaseMapper<ProjectSprayBind> {
    /**
     * 根据扬尘设备Id  查询当前绑定的喷淋
     * @param environmentId
     * @return
     */
    List<ProjectSpray> getListProjectSprayBind(Integer environmentId);
}
