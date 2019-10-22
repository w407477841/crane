package com.xingyun.equipment.admin.modular.device.service.impl;

import com.xingyun.equipment.admin.modular.device.model.ProjectWaterDaily;
import com.xingyun.equipment.admin.modular.device.dao.ProjectWaterDailyMapper;
import com.xingyun.equipment.admin.modular.device.service.IProjectWaterDailyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 电表每日统计
正常情况   读数= 明细中最后一条数据 ， 用电量=（ 明细中最后一条数据 减去 每日统计最后一条）
第一次      读数= 明细中最后一条数据 ， 用电量=（ 明细中最后一条数据 减去 明细当日第一条数据）
没有数据  读数=（用电量为每日统计最后一个读数） ，用电量 = 0 服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-11-20
 */
@Service
public class ProjectWaterDailyServiceImpl extends ServiceImpl<ProjectWaterDailyMapper, ProjectWaterDaily> implements IProjectWaterDailyService {

}
