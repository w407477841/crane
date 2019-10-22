package com.xingyun.equipment.crane.modular.device.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.crane.modular.device.model.ProjectLift;
import com.xingyun.equipment.crane.modular.device.model.ProjectLiftAlarm;
import com.xingyun.equipment.crane.modular.device.vo.ProjectLiftAlarmVO;
import com.xingyun.equipment.crane.modular.device.vo.ProjectLiftVO;
import com.xingyun.equipment.crane.modular.device.vo.RealTimeMonitoringTowerVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-22
 */
public interface ProjectLiftAlarmMapper extends BaseMapper<ProjectLiftAlarm> {
	/**
	 * 查询列表
	 * 
	 * @param rowBounds
	 * @param wrapper
	 * @return
	 */
	List<ProjectLiftAlarm> selectPageList(RowBounds rowBounds);
	/**
	 * 预警个数
	 * @param map
	 * @return
	 */
	List<ProjectLiftAlarmVO> countEarlyWarning(Map<String, Object> map);
	/**
	 * 预警详情
	 * @param rowBounds
	 * @param map
	 * @return
	 */
	List<ProjectLiftAlarmVO> earlyWarningDetail(RowBounds rowBounds, Map<String, Object> map);

	/**
	 * 实时监控查询
	 * 
	 * @param rowBounds
	 * @param c
	 * @return
	 */
	List<ProjectLift> realTimeMonitoring(Page<ProjectLift> page, @Param("ew") EntityWrapper<ProjectLift> ew, Map<String, Object> map);
	/**
     * 发送短信
     * @param realTimeMonitoringTowerVo
     * @return
     * @throws Exception
     */
    Integer insertMessage(RealTimeMonitoringTowerVo realTimeMonitoringTowerVo) throws Exception;

}
