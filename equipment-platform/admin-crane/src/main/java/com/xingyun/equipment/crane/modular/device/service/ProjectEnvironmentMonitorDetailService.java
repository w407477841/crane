package com.xingyun.equipment.crane.modular.device.service;

import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.core.vo.CurrentMonitorDataVO;
import com.xingyun.equipment.crane.core.vo.TrendItemVO;
import com.xingyun.equipment.crane.core.vo.WindDirectionTrendItemVO;
import com.xingyun.equipment.crane.modular.device.dto.OnlineDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentMonitorDetail;

import com.xingyun.equipment.crane.modular.device.vo.DeviceRankVO;
import com.xingyun.equipment.crane.modular.device.vo.EnvironmentDeviceVO;
import com.xingyun.equipment.crane.modular.device.vo.TrendVO;
import org.omg.CORBA.INTERNAL;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@Service
public interface ProjectEnvironmentMonitorDetailService extends IService<ProjectEnvironmentMonitorDetail> {
    ResultDTO<DataDTO<List<ProjectEnvironmentMonitorDetail>>> selectPageList(RequestDTO<ProjectEnvironmentMonitorDetail> res);

    /**
     *  趋势分表
     * @param uuid
     * @param deviceNo
     * @return
     */
    List<TrendItemVO>  trend(String uuid, String deviceNo, String columnName);


    /**
     * 趋势分表
     * @param uuid
     * @param deviceNo
     * @param columnName
     * @param startTime
     * @param endTime
     * @return
     */
    List<TrendItemVO>  trend(String uuid, String deviceNo, String columnName, String startTime, String endTime);

    /**
     * 风向 趋势分表
     * @param uuid
     * @param deviceNo
     * @return
     */
    List<WindDirectionTrendItemVO>  windDirectionTrend(String uuid, String deviceNo);
    List<WindDirectionTrendItemVO>  windDirectionTrend(String uuid, String deviceNo, String columnName, String startTime, String endTime);

    /**
     * 近期数据
     * @param uuid
     * @param deviceNo
     * @return
     */
    CurrentMonitorDataVO getMonitorData(String uuid, String deviceNo);

    /**
     * 近期数据
     * @param param
     * @return
     */
    EnvironmentDeviceVO getLastOne(Map<String, Object> param);

    /**
     * 近期数据
     * @param projectId
     * @param deviceNo
     * @param columnName
     * @return
     */
    List<TrendVO> getTrend(int projectId, String deviceNo, String columnName);

    /**
     * 获取前20
     * @param param
     * @return
     */
    List<DeviceRankVO> getRank20(Map<String, Object> param);


    /**
     * 风向 趋势分表
     * @param projectId
     * @param deviceNo
     * @return
     */
    List<WindDirectionTrendItemVO>  windDirectionTrendForScreen(String projectId, String deviceNo);

    /**
     *   趋势分表
     * @param projectId
     * @param deviceNo
     * @param columnName
     * @return
     */
    List<TrendItemVO>  trendForScreen(String projectId, String deviceNo, String columnName);


    List<OnlineDTO>  getOfflines(String uuids[], String type, Integer online);


    ResultDTO<DataDTO<List<Map<String,Object>>>> changeToChart(Integer id, String columnName, Integer type, String beginDate, String endDate);

    /**
     * 一天的PM2.5,PM10折线图数据
     * @param uuid
     * @param deviceNo
     * @return
     */
	ResultDTO<Map<String, Object>> getMonitorInfo(String uuid, String deviceNo);


}
