package com.xingyun.equipment.admin.modular.device.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.modular.device.model.ProjectCraneStatisticsDaily;
import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.admin.modular.device.vo.ProjectCraneStaticsticsDailyVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2019-06-19
 */
public interface IProjectCraneStatisticsDailyService extends IService<ProjectCraneStatisticsDaily> {

    List<ProjectCraneStaticsticsDailyVO> selectPageList(Page<ProjectCraneStaticsticsDailyVO> page, Map<String,Object> map);

    /**
     * 台账不分页
     * @param map
     * @return
     */
    List<ProjectCraneStaticsticsDailyVO> selectPageListNoPage(Map<String,Object> map);
}
