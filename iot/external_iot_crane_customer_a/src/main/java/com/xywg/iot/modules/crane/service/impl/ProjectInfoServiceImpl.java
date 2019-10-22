package com.xywg.iot.modules.crane.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.modules.crane.dao.ProjectInfoMapper;
import com.xywg.iot.modules.crane.model.ProjectInfo;
import com.xywg.iot.modules.crane.service.IProjectInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Service
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements IProjectInfoService {

}
