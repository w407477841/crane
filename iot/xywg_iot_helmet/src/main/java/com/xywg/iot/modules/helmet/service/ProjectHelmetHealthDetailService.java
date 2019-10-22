package com.xywg.iot.modules.helmet.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.modules.helmet.model.ProjectHelmetHealthDetail;

/**
 *  服务类
 *
 */
public interface ProjectHelmetHealthDetailService extends IService<ProjectHelmetHealthDetail> {

   void insertByMonth(ProjectHelmetHealthDetail projectHelmetHealthDetail);
}
