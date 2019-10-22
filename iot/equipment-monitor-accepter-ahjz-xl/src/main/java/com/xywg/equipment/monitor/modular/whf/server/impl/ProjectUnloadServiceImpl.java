package com.xywg.equipment.monitor.modular.whf.server.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectUnloadMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectUnload;
import com.xywg.equipment.monitor.modular.whf.server.IProjectUnloadService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卸料设备表 服务实现类
 * </p>
 *
 * @author hy
 * @since 2019-07-11
 */
@Service
public class ProjectUnloadServiceImpl extends ServiceImpl<ProjectUnloadMapper, ProjectUnload> implements IProjectUnloadService {
    public final static String DEVICE_INFO_SUFF="str:deviceinfo:unload";
}
