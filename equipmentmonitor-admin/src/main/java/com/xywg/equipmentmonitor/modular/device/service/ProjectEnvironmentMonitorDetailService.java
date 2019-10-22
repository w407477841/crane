package com.xywg.equipmentmonitor.modular.device.service;

import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.vo.CurrentMonitorDataVO;
import com.xywg.equipmentmonitor.core.vo.TrendItemVO;
import com.xywg.equipmentmonitor.core.vo.WindDirectionTrendItemVO;
import com.xywg.equipmentmonitor.modular.device.dto.OnlineDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitorDetail;

import com.xywg.equipmentmonitor.modular.device.vo.DeviceRankVO;
import com.xywg.equipmentmonitor.modular.device.vo.EnvironmentDeviceVO;
import com.xywg.equipmentmonitor.modular.device.vo.TrendVO;
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
    List<TrendItemVO>  trend(String uuid,String deviceNo,String columnName);


    /**
     * 趋势分表
     * @param uuid
     * @param deviceNo
     * @param columnName
     * @param startTime
     * @param endTime
     * @return
     */
    List<TrendItemVO>  trend(String uuid,String deviceNo,String columnName,String startTime ,String endTime);

    /**
     * 风向 趋势分表
     * @param uuid
     * @param deviceNo
     * @return
     */
    List<WindDirectionTrendItemVO>  windDirectionTrend(String uuid,String deviceNo);
    List<WindDirectionTrendItemVO>  windDirectionTrend(String uuid,String deviceNo,String columnName,String startTime ,String endTime);

    /**
     * 近期数据，根据uuid查询
     * @param uuid
     * @param deviceNo
     * @return
     */
    CurrentMonitorDataVO getMonitorData(String uuid, String deviceNo);
    /**
     * 近期数据，根据projectId查询
     * @param projectId
     * @param deviceNo
     * @return
     */
    CurrentMonitorDataVO getMonitorData2(int projectId, String deviceNo);
    /**
     * 近期数据
     * @param param
     * @return
     */
    EnvironmentDeviceVO getLastOne(Map<String,Object> param);

    /**
     * 近期数据
     * @param projectId
     * @param deviceNo
     * @param columnName
     * @return
     */
    List<TrendVO> getTrend(int projectId,String deviceNo,String columnName);

    /**
     * 获取前20
     * @param param
     * @return
     */
    List<DeviceRankVO> getRank20(Map<String,Object> param);


    /**
     * 风向 趋势分表
     * @param projectId
     * @param deviceNo
     * @return
     */
    List<WindDirectionTrendItemVO>  windDirectionTrendForScreen(String projectId,String deviceNo);
    /**
     * 风向 趋势分表
     * @param projectId
     * @param deviceNo
     * @return
     */
    List<WindDirectionTrendItemVO>  mywindDirectionTrendForScreen(String projectId,String deviceNo);
    /**
     *   趋势分表
     * @param projectId
     * @param deviceNo
     * @param columnName
     * @return
     */
    List<TrendItemVO>  trendForScreen(String projectId,String deviceNo,String columnName);


    List<OnlineDTO>  getOfflines(String uuids[], String type, Integer online);


    ResultDTO<DataDTO<List<Map<String,Object>>>> changeToChart(Integer id,String columnName,Integer type,String beginDate, String endDate);

    /**
     * 一天的PM2.5,PM10折线图数据
     * @param uuid
     * @param deviceNo
     * @return
     */
	ResultDTO<Map<String, Object>> getMonitorInfo(String uuid, String deviceNo);


    /**
     * 智慧工地 扬尘信息
     * @return
     */
    ResultDTO<Object> getMonitorInfo2zhgd(String uuid, String deviceNo,Integer alarmId ,String time);
}
