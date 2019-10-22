package com.xywg.equipmentmonitor.modular.projectmanagement.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectArea;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectAreaVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-08-23
 */
public interface IProjectAreaService extends IService<ProjectArea> {
    /**
     * 获取区域列表
     * @return
     * @throws Exception
     */
    List<ProjectAreaVo> selectArea() throws Exception;
}
