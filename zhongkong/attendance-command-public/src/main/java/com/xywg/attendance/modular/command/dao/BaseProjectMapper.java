package com.xywg.attendance.modular.command.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.attendance.modular.command.model.BaseProject;
import com.xywg.attendance.modular.command.model.PerInf;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
public interface BaseProjectMapper extends BaseMapper<BaseProject> {
    List<BaseProject> getBaseProjectInfo(String name);
}
