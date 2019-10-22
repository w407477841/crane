package com.xywg.iot.modules.helmet.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.modules.helmet.model.ProjectHelmetPositionDetail;

/**
 *  服务类
 *
 */
public interface ProjectHelmetPositionDetailService extends IService<ProjectHelmetPositionDetail> {

    void insertByMonth(String tableName,ProjectHelmetPositionDetail projectHelmetPositionDetail);
}
