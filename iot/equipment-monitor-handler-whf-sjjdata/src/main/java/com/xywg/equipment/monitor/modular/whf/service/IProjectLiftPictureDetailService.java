package com.xywg.equipment.monitor.modular.whf.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftDataModel;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftPictureDetail;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyj
 * @since 2019-08-02
 */
public interface IProjectLiftPictureDetailService extends IService<ProjectLiftPictureDetail> {

     void insertImg(ProjectLiftPictureDetail detail);

}
