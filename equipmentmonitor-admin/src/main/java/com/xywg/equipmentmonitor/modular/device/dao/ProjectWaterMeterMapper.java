package com.xywg.equipmentmonitor.modular.device.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectWaterMeter;
import com.xywg.equipmentmonitor.modular.device.model.ProjectWaterMeterAlarm;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectWaterMeterAlarmVo;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectWaterMeterDetailVo;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectWaterMeterDeviceSumVo;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectWaterMeterHeartbeatVo;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectWaterMeterLineDataVo;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectWaterMeterPieDataVo;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectWaterMeterUseDataVo;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectWaterMeterVo;
import com.xywg.equipmentmonitor.modular.device.vo.WaterAlarmVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-09-27
 */
public interface ProjectWaterMeterMapper extends BaseMapper<ProjectWaterMeter> {
    /**
     * 获取实时监控水表信息
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterVo> selectWaterData(Page<ProjectWaterMeterVo> page,Map<String, Object> map) throws Exception;

    /**
     * 获取监控状态信息
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterHeartbeatVo> selectMonitorStatus(Page<ProjectWaterMeterHeartbeatVo> page,Map<String,Object> map) throws Exception;

    /**
     * 获取运行数据信息
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterDetailVo> selectRunData(Page<ProjectWaterMeterDetailVo> page,Map<String,Object> map) throws Exception;

    /**
     * 获取报警条数
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterAlarmVo> selectWarningAlarm(Page<ProjectWaterMeterAlarmVo> page,Map<String,Object> map) throws Exception;

    /**
     * 获取报警信息
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterAlarm> selectAlarmData(Map<String,Object> map) throws Exception;
	   /**
	    * 安全数据
	    */
    ProjectWaterMeterDeviceSumVo selectDeviceSum(RequestDTO res);
   /**
        *曲线图数据  
    */
    ProjectWaterMeterLineDataVo selectLineData(@Param("yearMonth") String yearMonth,@Param("tableName") String tableName);
   /**
    * 饼图数据   
    */
    List<ProjectWaterMeterPieDataVo> selectPieData(RequestDTO res);
    /**
    *安全数据    
   */
    ProjectWaterMeterUseDataVo selectUseData(RequestDTO res);
    List<ProjectWaterMeterAlarm> selectAlarmData(Page<ProjectWaterMeterAlarm> page,Map<String,Object> map) throws Exception;

    /**
     * 获取列表
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<ProjectWaterMeterVo> selectWater(Page<ProjectWaterMeterVo> page,Map<String,Object> map) throws Exception;
	 /**
	 * 分页查询
	 * @param rowBounds
	 * @param ew
	 * @return
	 */
	List<ProjectWaterMeter> selectPageList(RowBounds rowBounds,@Param("ew")EntityWrapper<RequestDTO<ProjectWaterMeter>> ew);
		
	/**
	 * 根据日期算水量
	 * @param month1
	 * @param month2
	 * @param day1
	 * @param day2
	 * @param ids 
	 * @return
	 */
	BigDecimal getAmountByDays1(@Param("month1")String month1, @Param("month2")String month2, @Param("day1")String day1,
			@Param("day2")String day2, @Param("ids")List<Integer> ids);
	/**
	 * 根据设备算水量
	 * @param begin
	 * @param end
	 * @param day1
	 * @param day2
	 * @param id
	 * @return
	 */
	BigDecimal getAmountByDevice1(@Param("month1")String begin, @Param("month2")String end, @Param("day1")String day1,
			@Param("day2")String day2, @Param("electricId")Integer id);
	/**
	 * 根据月份算水量
	 * @param month1
	 * @param month2
	 * @param ids
	 * @return
	 */
	BigDecimal getAmountByMonth1(@Param("month1")String month1, @Param("month2")String month2, @Param("ids")List<Integer> ids);
	
	/**
	 * 根据月份算水量(new)
	 * @param statisticsDate
	 * @param ids
	 * @return
	 */
	BigDecimal getAmountByMonth(@Param("statisticsDate")String statisticsDate, @Param("ids")List<Integer> ids);
	/**
	 * 根据日期算水量(new)
	 * @param beginDay
	 * @param endDay
	 * @param ids 
	 * @return
	 */
	BigDecimal getAmountByDays(@Param("beginDay")String beginDay, @Param("endDay")String endDay, @Param("ids")List<Integer> ids);
	/**
	 * 根据设备算水量
	 * @param beginDay
	 * @param endDay
	 * @param id
	 * @return
	 */
	BigDecimal getAmountByDevice(@Param("beginDay")String beginDay, @Param("endDay")String endDay, @Param("id")Integer id);
	/**
	 * 查询异常表数量
	 * @param month
	 * @param ids
	 * @return
	 */
	Integer selectAbnormal(@Param("month")String month,@Param("ids")List<Integer> ids);
	/**
	 * 获取不分页列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<ProjectWaterMeterVo> selectWaterList(Map<String,Object> map) throws Exception;
	/**
	 * 智慧工地绿色施工拉取数据
	 * @param page
	 * @param map
	 * @return
	 */
	 List<ProjectWaterMeterVo> getWaterDetailInfo(Page<ProjectWaterMeter> page,Map<String,Object> map);
	 
	 /**
	  * 拉取报警信息接口
	  * @param page
	  * @param map
	  * @return
	  */
	 List<WaterAlarmVO> getAlarmInfo(Page<WaterAlarmVO> page,Map<String,Object> map);
	 /**
	  * 拉取报警信息详情接口
	  * @param map
	  * @return
	  */
	 List<WaterAlarmVO> getAlarmDetail(Page<WaterAlarmVO> page,Map<String,Object> map);
}
