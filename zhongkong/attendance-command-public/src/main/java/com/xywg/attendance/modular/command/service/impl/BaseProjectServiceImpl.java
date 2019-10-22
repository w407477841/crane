package com.xywg.attendance.modular.command.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.attendance.modular.command.dao.BaseProjectMapper;
import com.xywg.attendance.modular.command.dao.PerInfMapper;
import com.xywg.attendance.modular.command.model.BaseProject;
import com.xywg.attendance.modular.command.model.PerInf;
import com.xywg.attendance.modular.command.service.BaseProjectService;
import com.xywg.attendance.modular.command.service.IPerInfService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
@Service
public class BaseProjectServiceImpl extends ServiceImpl<BaseProjectMapper, BaseProject> implements BaseProjectService {

    @Override
    public List<BaseProject> getBaseProjectInfo(String name) {
        return baseMapper.getBaseProjectInfo(name);
    }
}
