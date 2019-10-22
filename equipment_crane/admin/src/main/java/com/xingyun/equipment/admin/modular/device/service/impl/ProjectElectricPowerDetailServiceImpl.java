package com.xingyun.equipment.admin.modular.device.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.core.util.BigDecimalUtil;
import com.xingyun.equipment.admin.core.util.DateUtils;
import com.xingyun.equipment.admin.core.util.NumUtil;
import com.xingyun.equipment.admin.core.util.RedisUtil;
import com.xingyun.equipment.admin.core.util.StringCompress;
import com.xingyun.equipment.admin.modular.common.dto.AppResultDTO;
import com.xingyun.equipment.admin.modular.device.dao.ProjectElectricPowerDetailMapper;
import com.xingyun.equipment.admin.modular.device.dto.ElectricityManageDTO;
import com.xingyun.equipment.admin.modular.device.dto.WaterManageDTO;
import com.xingyun.equipment.admin.modular.device.model.ElectricityChangeInfo;
import com.xingyun.equipment.admin.modular.device.model.ElectricityTrend;
import com.xingyun.equipment.admin.modular.device.model.ElectricityType;
import com.xingyun.equipment.admin.modular.device.model.ProjectElectricPowerDetail;
import com.xingyun.equipment.admin.modular.device.model.WaterChangeInfo;
import com.xingyun.equipment.admin.modular.device.model.WaterTrend;
import com.xingyun.equipment.admin.modular.device.model.WaterType;
import com.xingyun.equipment.admin.modular.device.service.ProjectElectricPowerDetailService;
import com.xingyun.equipment.admin.modular.projectmanagement.service.IProjectInfoService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-09-28
 */
@Service
public class ProjectElectricPowerDetailServiceImpl
		extends ServiceImpl<ProjectElectricPowerDetailMapper, ProjectElectricPowerDetail>
		implements ProjectElectricPowerDetailService {

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IProjectInfoService projectInfoService;

	@Override
	public ResultDTO<DataDTO<List<ProjectElectricPowerDetail>>> selectPageList(
			RequestDTO<ProjectElectricPowerDetail> res) {
		String initialDate = DateUtils.getDate();
		ProjectElectricPowerDetail detail = res.getBody();
		String beginTime = detail.getBeginTime();
		String endTime = detail.getEndTime();
		if (StringUtils.isBlank(beginTime)
				|| DateUtils.parseDate(beginTime).getTime() < DateUtils.parseDate(initialDate).getTime()) {
			detail.setBeginTime(initialDate);
		}
		if (StringUtils.isBlank(endTime) || DateUtils.parseDate(endTime).getTime() > System.currentTimeMillis()) {
			detail.setEndTime(DateUtils.formatDateTime(new Date()));
		} else if (DateUtils.parseDate(endTime).getTime() < DateUtils.parseDate(initialDate).getTime()) {
			detail.setEndTime(initialDate);
		}
		// 两个时间段内的月份
		List<String> months = DateUtils.getMonthsBetween(DateUtils.parseDate(detail.getBeginTime()),
				DateUtils.parseDate(detail.getEndTime()));
		Map<String, Object> map = new HashMap<>();
		map.put("months", months);
		map.put("startTime", detail.getBeginTime());
		map.put("endTime", detail.getEndTime());
		map.put("electricId", detail.getElectricId());
		map.put("status", detail.getStatus());
		Page<ProjectElectricPowerDetail> page = new Page<>(res.getPageNum(), res.getPageSize());
		List<ProjectElectricPowerDetail> list = baseMapper.selectPageList(page, map);
		return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public byte[] getElectricInfo(RequestDTO request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<String> uuidList = NumUtil.stringToList(request.getUuid());
		// 用电管理DTO
		ElectricityManageDTO electricityManageDTO = new ElectricityManageDTO();
		// 用电同比/环比对象
		ElectricityChangeInfo electricityChangeInfo = new ElectricityChangeInfo();
		// 当月的电表名
		String tableName = "t_project_electric_power_detail_" + DateUtils.getYearMonth();
		// 上月电表名
		String tableNameLast = "t_project_electric_power_detail_" + DateUtils.getLastMonth();
		// 上上月表名
		String tableName2Last = "t_project_electric_power_detail_" + DateUtils.getLast2Month();
		// 去年当月电表名
		String tableNameLastYearMonth = "t_project_electric_power_detail_" + DateUtils.getLastYearMonth();
		// 去年上月电表名
		String tableNameLastYear2Month = "t_project_electric_power_detail_" + DateUtils.getLastYear2Month();
		// 用电总量
		BigDecimal totalElectricity = BigDecimal.ZERO;
		// 当月用电量
		BigDecimal currentMonthElectricity = BigDecimal.ZERO;
		// 上月用电量
		BigDecimal lastMonthElectricity = BigDecimal.ZERO;
		// 上上月用电量
		BigDecimal lastLastMonthElectricity = BigDecimal.ZERO;
		// 去年当月用电量
		BigDecimal lastYearMonthElectricity = BigDecimal.ZERO;
		// 去年上月用电量
		BigDecimal lastYearLastMonthElectricity = BigDecimal.ZERO;
		// 去年当月用电对象
		ElectricityType electricityTypeLastYearMonth;
		// 去年上月用电对象
		ElectricityType electricityTypeLastYearLast;
		// 传入项目uuid
		map.put("list", uuidList);
		// 传入当月表名
		map.put("tableName", tableName);
		// 查询用电分类信息
		List<ElectricityType> electricityTypeListBefore = baseMapper.selectElectricityType(map);
		List<ElectricityType> electricityTypeList = new ArrayList<ElectricityType>();
		for (ElectricityType bean : electricityTypeListBefore) {
			totalElectricity = BigDecimalUtil.safeAdd(totalElectricity, bean.getElectricity());
		}
		for (ElectricityType bean : electricityTypeListBefore) {
			BigDecimal proportion = BigDecimalUtil.safeDivide(bean.getElectricity(), totalElectricity, BigDecimal.ZERO);
			ElectricityType electricityType = new ElectricityType();
			electricityType.setTypeName(bean.getTypeName());
			electricityType.setElectricity(proportion);
			electricityTypeList.add(electricityType);
		}

		// 查询7天用电趋势
		List<ElectricityTrend> trendList = baseMapper.selectElectricityTrend(map);
		// 当月用电量返回对象
		ElectricityType electricityType = baseMapper.selectElectricityByMonth(map);
		if (null != electricityType) {
			currentMonthElectricity = electricityType.getElectricity();
		}
		// 传入上个月表名
		map.put("tableName", tableNameLast);
		// 上月用电量返回对象
		ElectricityType electricityTypeLast = baseMapper.selectElectricityByMonth(map);
		if (null != electricityTypeLast) {
			lastMonthElectricity = electricityTypeLast.getElectricity();
		}

		// 当前日期小于7号时需要查询上个月的用电表
		if (DateUtils.getToday() < 07) {
			// 需要从上个月取几号之后的数据
			String lastMonthDay = DateUtils.getLastMonthDay(7 - DateUtils.getToday());
			map.put("lastMonthDay", lastMonthDay);
			List<ElectricityTrend> lastTrendList = baseMapper.selectLastElectricityTrend(map);
			if (CollectionUtil.isNotEmpty(lastTrendList)) {
				trendList = trendList.subList(0, DateUtils.getToday());
				trendList.addAll(lastTrendList);
			}
		}
		// 传入上上个月表名
		map.put("tableName", tableName2Last);
		// 上上月用电量返回对象
		ElectricityType electricityType2Last = baseMapper.selectElectricityByMonth(map);
		if (null != electricityType2Last) {
			lastLastMonthElectricity = electricityType2Last.getElectricity();
		}
		// 传入去年当月表名
		map.put("tableName", tableNameLastYearMonth);
		try {
			electricityTypeLastYearMonth = baseMapper.selectElectricityByMonth(map);
		} catch (Exception e) {
			electricityTypeLastYearMonth = null;
		}
		if (null != electricityTypeLastYearMonth) {
			lastYearMonthElectricity = electricityTypeLastYearMonth.getElectricity();
		}
		// 传入去年上个月表名
		map.put("tableName", tableNameLastYear2Month);
		// 去年上月用电量返回对象
		try {
			electricityTypeLastYearLast = baseMapper.selectElectricityByMonth(map);
		} catch (Exception e) {
			electricityTypeLastYearLast = null;
		}
		if (null != electricityTypeLastYearLast) {
			lastYearLastMonthElectricity = electricityTypeLastYearLast.getElectricity();
		}
		// 环比值
		BigDecimal sequential = BigDecimalUtil.safeDivide(
				BigDecimalUtil.safeSubtract(BigDecimalUtil.safeSubtract(currentMonthElectricity, lastMonthElectricity),
						BigDecimalUtil.safeSubtract(lastMonthElectricity, lastLastMonthElectricity)),
				BigDecimalUtil.safeSubtract(lastMonthElectricity, lastLastMonthElectricity), BigDecimal.ZERO);
		// 同比值
		BigDecimal yoy = BigDecimalUtil.safeDivide(
				BigDecimalUtil.safeSubtract(BigDecimalUtil.safeSubtract(currentMonthElectricity, lastMonthElectricity),
						BigDecimalUtil.safeSubtract(lastYearMonthElectricity, lastYearLastMonthElectricity)),
				BigDecimalUtil.safeSubtract(lastYearMonthElectricity, lastYearLastMonthElectricity), BigDecimal.ZERO);
		electricityChangeInfo.setSequential(sequential);
		electricityChangeInfo.setYoy(yoy);
		electricityManageDTO.setElectricityChangeInfo(electricityChangeInfo);
		electricityManageDTO.setElectricityTypeList(electricityTypeList);
		Collections.reverse(trendList);
		electricityManageDTO.setTrendList(trendList);
		String resultStr = JSONUtil.toJsonStr(electricityManageDTO);
		return StringCompress.compress(resultStr);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public byte[] getElectricInfos(RequestDTO request) throws Exception {
		List<String> uuidList = NumUtil.stringToList(request.getUuid());
		// 用电管理DTO
		ElectricityManageDTO electricityManageDTO = new ElectricityManageDTO();
		// 用电同比/环比对象
		ElectricityChangeInfo electricityChangeInfo = new ElectricityChangeInfo();
		// 当月
		String currentMonth = DateUtils.getYearMonth();
		// 上月
		String currentMonthLast = DateUtils.getLastMonth();
		// 去年当月
		String lastCurrentMonth = DateUtils.getLastYearMonth();
		// 用电总量
		BigDecimal totalElectricity = BigDecimal.ZERO;
		// 当月用电量
		BigDecimal currentMonthElectricity = BigDecimal.ZERO;
		// 上月用电量
		BigDecimal lastMonthElectricity = BigDecimal.ZERO;
		// 去年当月用电量
		BigDecimal lastYearMonthElectricity = BigDecimal.ZERO;

		// 查询用电分类信息
		List<ElectricityType> electricityTypeListBefore = baseMapper.selectElectrType(uuidList);
		List<ElectricityType> electricityTypeList = new ArrayList<ElectricityType>();
		for (ElectricityType bean : electricityTypeListBefore) {
			totalElectricity = BigDecimalUtil.safeAdd(totalElectricity, bean.getElectricity());
		}
		for (ElectricityType bean : electricityTypeListBefore) {
			BigDecimal proportion = BigDecimalUtil.safeDivide(bean.getElectricity(), totalElectricity, BigDecimal.ZERO);
			ElectricityType electricityType = new ElectricityType();
			electricityType.setTypeName(bean.getTypeName());
			electricityType.setElectricity(proportion);
			electricityTypeList.add(electricityType);
		}
		// 查询生活用电7天用电趋势
		List<ElectricityTrend> lifeTrendList = baseMapper.selectElectrTrend(uuidList, 1);
		// 查询生产用电7天用电趋势
		List<ElectricityTrend> proTrendList = baseMapper.selectElectrTrend(uuidList, 2);
		List<ElectricityTrend> trendList = new ArrayList<ElectricityTrend>();
		LinkedHashMap<String,Object> map = Maps.newLinkedHashMap();
		lifeTrendList.forEach(bean->{
        	map.put(bean.getTrendDate(), bean.getElectricity());
		});
		proTrendList.forEach(bean->{
        	if(map.containsKey(bean.getTrendDate())) {
        		ElectricityTrend electricityTrend = new ElectricityTrend();
        		electricityTrend.setTrendDate(bean.getTrendDate());
        		electricityTrend.setElectricity(BigDecimalUtil.safeAdd(bean.getElectricity(), (BigDecimal)map.get(bean.getTrendDate())));
        		trendList.add(electricityTrend);
        	}
		});
		// 按月查询用电量
		ElectricityType currentMonthElectricityType = baseMapper.selectElectrByMonth(currentMonth);
		ElectricityType currentMonthLastElectricityType = baseMapper.selectElectrByMonth(currentMonthLast);
		ElectricityType lastCurrentMonthElectricityType = baseMapper.selectElectrByMonth(lastCurrentMonth);
		if (null != currentMonthElectricityType) {
			currentMonthElectricity = currentMonthElectricityType.getElectricity();
		}
		if (null != currentMonthLastElectricityType) {
			lastMonthElectricity = currentMonthLastElectricityType.getElectricity();
		}
		if (null != lastCurrentMonthElectricityType) {
			lastYearMonthElectricity = lastCurrentMonthElectricityType.getElectricity();
		}
		// 环比值
		BigDecimal sequential = BigDecimalUtil.safeDivide(
				BigDecimalUtil.safeSubtract(currentMonthElectricity, lastMonthElectricity), lastMonthElectricity,
				BigDecimal.ZERO);
		// 同比值
		BigDecimal yoy = BigDecimalUtil.safeDivide(
				BigDecimalUtil.safeSubtract(currentMonthElectricity, lastYearMonthElectricity), lastYearMonthElectricity,
				BigDecimal.ZERO);
		electricityChangeInfo.setSequential(sequential);
		electricityChangeInfo.setYoy(yoy);
		electricityManageDTO.setElectricityChangeInfo(electricityChangeInfo);
		electricityManageDTO.setElectricityTypeList(electricityTypeList);
		electricityManageDTO.setLifeTrendList(lifeTrendList);
		electricityManageDTO.setProTrendList(proTrendList);
		electricityManageDTO.setTrendList(trendList);
		String resultStr = JSONUtil.toJsonStr(AppResultDTO.factory(electricityManageDTO));
		return StringCompress.compress(resultStr);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public byte[] getWaterInfos(RequestDTO request) throws Exception {
		List<String> uuidList = NumUtil.stringToList(request.getUuid());
		// 用电管理DTO
		WaterManageDTO waterManageDTO = new WaterManageDTO();
		// 用电同比/环比对象
		WaterChangeInfo waterChangeInfo = new WaterChangeInfo();
		// 当月
		String currentMonth = DateUtils.getYearMonth();
		// 上月
		String currentMonthLast = DateUtils.getLastMonth();
		// 去年当月
		String lastCurrentMonth = DateUtils.getLastYearMonth();
		// 用电总量
		BigDecimal totalWater = BigDecimal.ZERO;
		// 当月用电量
		BigDecimal currentMonthWater = BigDecimal.ZERO;
		// 上月用电量
		BigDecimal lastMonthWater = BigDecimal.ZERO;
		// 去年当月用电量
		BigDecimal lastYearMonthWater = BigDecimal.ZERO;

		// 查询用电分类信息
		List<WaterType> waterTypeListBefore = baseMapper.selectWatType(uuidList);
		List<WaterType> electricityTypeList = new ArrayList<WaterType>();
		;
		for (WaterType bean : waterTypeListBefore) {
			totalWater = BigDecimalUtil.safeAdd(totalWater, bean.getWater());
		}
		for (WaterType bean : waterTypeListBefore) {
			BigDecimal proportion = BigDecimalUtil.safeDivide(bean.getWater(), totalWater, BigDecimal.ZERO);
			WaterType waterType = new WaterType();
			waterType.setTypeName(bean.getTypeName());
			waterType.setWater(proportion);
			electricityTypeList.add(waterType);
		}
		// 查询生活用水7天用水趋势
		List<WaterTrend> lifeTrendList = baseMapper.selectWatTrend(uuidList, 1);
		// 查询生产用水7天用水趋势
		List<WaterTrend> proTrendList = baseMapper.selectWatTrend(uuidList, 2);
		// 查询消防用水7天用水趋势
		List<WaterTrend> fireTrendList = baseMapper.selectWatTrend(uuidList, 3);
		
		List<WaterTrend> beforeTrendList = new ArrayList<WaterTrend>();
		List<WaterTrend> trendList = new ArrayList<WaterTrend>();
		LinkedHashMap<String,Object> map = Maps.newLinkedHashMap();
		lifeTrendList.forEach(bean->{
        	map.put(bean.getTrendDate(), bean.getWater());
		});
		proTrendList.forEach(bean->{
        	if(map.containsKey(bean.getTrendDate())) {
        		WaterTrend waterTrend = new WaterTrend();
        		waterTrend.setTrendDate(bean.getTrendDate());
        		waterTrend.setWater(BigDecimalUtil.safeAdd(bean.getWater(), (BigDecimal)map.get(bean.getTrendDate())));
        		beforeTrendList.add(waterTrend);
        	}
		});
		beforeTrendList.forEach(bean->{
        	map.put(bean.getTrendDate(), bean.getWater());
		});
		fireTrendList.forEach(bean->{
			if(map.containsKey(bean.getTrendDate())) {
        		WaterTrend waterTrend = new WaterTrend();
        		waterTrend.setTrendDate(bean.getTrendDate());
        		waterTrend.setWater(BigDecimalUtil.safeAdd(bean.getWater(), (BigDecimal)map.get(bean.getTrendDate())));
        		trendList.add(waterTrend);
        	}
		});
		// 按月查询用电量
		WaterType currentMonthWaterType = baseMapper.selectWatByMonth(currentMonth);
		WaterType currentMonthLastWaterType = baseMapper.selectWatByMonth(currentMonthLast);
		WaterType lastCurrentMonthWaterType = baseMapper.selectWatByMonth(lastCurrentMonth);
		if (null != currentMonthWaterType) {
			currentMonthWater = currentMonthWaterType.getWater();
		}
		if (null != currentMonthLastWaterType) {
			lastMonthWater = currentMonthLastWaterType.getWater();
		}
		if (null != lastCurrentMonthWaterType) {
			lastYearMonthWater = lastCurrentMonthWaterType.getWater();
		}
		// 环比值
		BigDecimal sequential = BigDecimalUtil.safeDivide(
				BigDecimalUtil.safeSubtract(currentMonthWater, lastMonthWater), lastMonthWater,
				BigDecimal.ZERO);
		// 同比值
		BigDecimal yoy = BigDecimalUtil.safeDivide(
				BigDecimalUtil.safeSubtract(currentMonthWater, lastYearMonthWater), lastYearMonthWater,
				BigDecimal.ZERO);
		waterChangeInfo.setSequential(sequential);
		waterChangeInfo.setYoy(yoy);
		waterManageDTO.setWaterChangeInfo(waterChangeInfo);
		waterManageDTO.setWaterTypeList(electricityTypeList);
		waterManageDTO.setLifeTrendList(lifeTrendList);
		waterManageDTO.setProTrendList(proTrendList);
		waterManageDTO.setFireTrendList(fireTrendList);
		waterManageDTO.setTrendList(trendList);
		String resultStr = JSONUtil.toJsonStr(AppResultDTO.factory(waterManageDTO));
		return StringCompress.compress(resultStr);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public byte[] getWaterInfo(RequestDTO request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<String> uuidList = NumUtil.stringToList(request.getUuid());
		WaterManageDTO waterManageDTO = new WaterManageDTO();
		WaterChangeInfo waterChangeInfo = new WaterChangeInfo();
		String tableName = "t_project_water_meter_detail_" + DateUtils.getYearMonth();
		String tableNameLast = "t_project_water_meter_detail_" + DateUtils.getLastMonth();
		String tableNameLastYearMonth = "t_project_water_meter_detail_" + DateUtils.getLastYearMonth();
		// 上上月表名
		String tableName2Last = "t_project_water_meter_detail_" + DateUtils.getLast2Month();
		// 去年上月表名
		String tableNameLastYear2Month = "t_project_water_meter_detail_" + DateUtils.getLastYear2Month();
		// 用水总量
		BigDecimal totalWater = BigDecimal.ZERO;
		BigDecimal currentMonthWater = BigDecimal.ZERO;
		BigDecimal lastMonthWater = BigDecimal.ZERO;
		// 上上月用电量
		BigDecimal lastLastMonthWater = BigDecimal.ZERO;
		BigDecimal lastYearMonthWater = BigDecimal.ZERO;
		// 去年上月用电量
		BigDecimal lastYearLastMonthWater = BigDecimal.ZERO;
		WaterType waterTypeLastYearMonth;
		// 去年上月用水对象
		WaterType waterTypeLastYearLast;
		map.put("list", uuidList);
		map.put("tableName", tableName);
		List<WaterType> waterTypeListbefore = baseMapper.selectWaterType(map);
		List<WaterType> waterTypeList = baseMapper.selectWaterType(map);
		for (WaterType bean : waterTypeListbefore) {
			totalWater = BigDecimalUtil.safeAdd(totalWater, bean.getWater());
		}
		for (WaterType bean : waterTypeListbefore) {
			BigDecimal proportion = BigDecimalUtil.safeDivide(bean.getWater(), totalWater, BigDecimal.ZERO);
			WaterType waterType = new WaterType();
			waterType.setTypeName(bean.getTypeName());
			waterType.setWater(proportion);
			waterTypeList.add(waterType);
		}

		List<WaterTrend> trendList = baseMapper.selectWaterTrend(map);
		WaterType waterType = baseMapper.selectWaterByMonth(map);
		if (null != waterType) {
			currentMonthWater = waterType.getWater();
		}
		map.put("tableName", tableNameLast);
		WaterType waterTypeLast = baseMapper.selectWaterByMonth(map);
		if (null != waterTypeLast) {
			lastMonthWater = waterTypeLast.getWater();
		}

		// 当前日期小于7号时需要查询上个月的用水表
		if (DateUtils.getToday() < 07) {
			// 需要从上个月取几号之后的数据
			String lastMonthDay = DateUtils.getLastMonthDay(7 - DateUtils.getToday());
			map.put("lastMonthDay", lastMonthDay);
			List<WaterTrend> lastTrendList = baseMapper.selectLastWaterTrend(map);
			if (CollectionUtil.isNotEmpty(lastTrendList)) {
				trendList = trendList.subList(0, DateUtils.getToday());
				trendList.addAll(lastTrendList);
			}
		}

		// 传入上上个月表名
		map.put("tableName", tableName2Last);
		// 上上月用电量返回对象
		WaterType waterType2Last = baseMapper.selectWaterByMonth(map);
		if (null != waterType2Last) {
			lastLastMonthWater = waterType2Last.getWater();
		}

		map.put("tableName", tableNameLastYearMonth);
		try {
			waterTypeLastYearMonth = baseMapper.selectWaterByMonth(map);
		} catch (Exception e) {
			waterTypeLastYearMonth = null;
		}
		if (null != waterTypeLastYearMonth) {
			lastYearMonthWater = waterTypeLastYearMonth.getWater();
		}
		// 传入去年上个月表名
		map.put("tableName", tableNameLastYear2Month);
		// 去年上月用电量返回对象
		try {
			waterTypeLastYearLast = baseMapper.selectWaterByMonth(map);
		} catch (Exception e) {
			waterTypeLastYearLast = null;
		}
		if (null != waterTypeLastYearLast) {
			lastYearLastMonthWater = waterTypeLastYearLast.getWater();
		}
		// 环比值
		BigDecimal sequential = BigDecimalUtil.safeDivide(
				BigDecimalUtil.safeSubtract(BigDecimalUtil.safeSubtract(currentMonthWater, lastMonthWater),
						BigDecimalUtil.safeSubtract(lastMonthWater, lastLastMonthWater)),
				BigDecimalUtil.safeSubtract(lastMonthWater, lastLastMonthWater), BigDecimal.ZERO);
		// 同比值
		BigDecimal yoy = BigDecimalUtil.safeDivide(
				BigDecimalUtil.safeSubtract(BigDecimalUtil.safeSubtract(currentMonthWater, lastMonthWater),
						BigDecimalUtil.safeSubtract(lastYearMonthWater, lastYearLastMonthWater)),
				BigDecimalUtil.safeSubtract(lastYearMonthWater, lastYearLastMonthWater), BigDecimal.ZERO);
		waterChangeInfo.setSequential(sequential);
		waterChangeInfo.setYoy(yoy);
		waterManageDTO.setWaterChangeInfo(waterChangeInfo);
		waterManageDTO.setWaterTypeList(waterTypeList);
		Collections.reverse(trendList);
		waterManageDTO.setTrendList(trendList);
		String resultStr = JSONUtil.toJsonStr(waterManageDTO);
		return StringCompress.compress(resultStr);
	}

	
}
