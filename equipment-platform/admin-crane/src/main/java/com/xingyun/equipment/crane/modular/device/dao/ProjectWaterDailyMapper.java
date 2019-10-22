package com.xingyun.equipment.crane.modular.device.dao;

import com.xingyun.equipment.crane.modular.device.model.ProjectWaterDaily;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 电表每日统计
正常情况   读数= 明细中最后一条数据 ， 用电量=（ 明细中最后一条数据 减去 每日统计最后一条）
第一次      读数= 明细中最后一条数据 ， 用电量=（ 明细中最后一条数据 减去 明细当日第一条数据）
没有数据  读数=（用电量为每日统计最后一个读数） ，用电量 = 0 Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-11-20
 */
public interface ProjectWaterDailyMapper extends BaseMapper<ProjectWaterDaily> {

}
