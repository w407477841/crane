package com.xingyun.equipment.crane.modular.device.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.crane.modular.device.model.ProjectCraneStatisticsDaily;
import com.xingyun.equipment.crane.modular.device.dao.ProjectCraneStatisticsDailyMapper;
import com.xingyun.equipment.crane.modular.device.service.IProjectCraneStatisticsDailyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.crane.modular.device.vo.ProjectCraneStaticsticsDailyVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2019-06-19
 */
@Service
public class ProjectCraneStatisticsDailyServiceImpl extends ServiceImpl<ProjectCraneStatisticsDailyMapper, ProjectCraneStatisticsDaily> implements IProjectCraneStatisticsDailyService {

    @Override
    public List<ProjectCraneStaticsticsDailyVO> selectPageList(Page<ProjectCraneStaticsticsDailyVO> page, Map<String,Object> map) {

      return   baseMapper.selectPageList(page,map);

    }





    @Override
    public List<ProjectCraneStaticsticsDailyVO> selectPageListNoPage(Map<String,Object> map) {
        return   baseMapper.selectPageListNoPage(map);
    }
}
