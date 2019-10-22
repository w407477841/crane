package com.xywg.iot.modules.helmet.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.xywg.iot.modules.helmet.dao.ProjectHelmetMapper;
import com.xywg.iot.modules.helmet.model.ProjectHelmet;
import com.xywg.iot.modules.helmet.service.ProjectHelmetService;
import org.springframework.stereotype.Service;

/**
 * 安全帽
 * @author hjy
 */
@Service
public class ProjectHelmetServiceImpl extends ServiceImpl<ProjectHelmetMapper, ProjectHelmet> implements ProjectHelmetService {

}
