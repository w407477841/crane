package com.xywg.equipment.monitor.modular.whf.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectLiftDataModelMapper;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectLiftPictureDetailMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftDataModel;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftPictureDetail;
import com.xywg.equipment.monitor.modular.whf.service.IProjectLiftDataModelService;
import com.xywg.equipment.monitor.modular.whf.service.IProjectLiftPictureDetailService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyj
 * @since 2019-08-02
 */
@Service
public class ProjectLiftPictureDetailServiceImpl extends ServiceImpl<ProjectLiftPictureDetailMapper, ProjectLiftPictureDetail> implements IProjectLiftPictureDetailService {

    @Override
    public void insertImg(ProjectLiftPictureDetail detail) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        Date now = new Date();
        String month = sdf.format(now).substring(0,6);
        baseMapper.insertImg(detail,month);


    }
}
