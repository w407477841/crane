package com.xywg.attendance.modular.command.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.attendance.modular.command.model.BaseProject;
import com.xywg.attendance.modular.command.model.PerInf;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
public interface BaseProjectService extends IService<BaseProject> {

    List<BaseProject> getBaseProjectInfo(String name);

}
