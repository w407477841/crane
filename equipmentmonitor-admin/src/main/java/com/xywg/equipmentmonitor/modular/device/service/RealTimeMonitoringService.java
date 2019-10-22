package com.xywg.equipmentmonitor.modular.device.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.vo.CurrentCraneDataVO;
import com.xywg.equipmentmonitor.modular.device.vo.RealTimeMonitoringTowerVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/***
 *@author:jixiaojun
 *DATE:2018/8/23
 *TIME:9:28
 */
@Service
public interface RealTimeMonitoringService extends IService<RealTimeMonitoringTowerVo> {
    /**
     * 获取塔吊信息
     * @param page
     * @param resultDTO
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectCrane(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> resultDTO) throws Exception;

    /**
     * 获取塔吊详细数据
     * @param page
     * @param resultDTO
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectCraneData(Page<RealTimeMonitoringTowerVo> page, RequestDTO<RealTimeMonitoringTowerVo> resultDTO) throws Exception;

    /**
     * 获取预警条数
     * @param page
     * @param resultDTO
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectPromptingAlarm(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> resultDTO) throws Exception;

    /**
     * 获取报警条数
     * @param page
     * @param resultDTO
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectWarningAlarm(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> resultDTO) throws Exception;

    /**
     * 获取违章条数
     * @param page
     * @param resultDTO
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectViolationAlarm(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> resultDTO) throws Exception;

    /**
     * 获取信息详情
     * @param page
     * @param resultDTO
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectAlarmData(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> resultDTO) throws Exception;

    /**
     * 发送短信
     * @param realTimeMonitoringTowerVo
     * @return
     * @throws Exception
     */
    boolean insertMessage(RealTimeMonitoringTowerVo realTimeMonitoringTowerVo) throws Exception;

    /**
     * 获取塔吊信息给智慧工地
     * @param map
     * @return
     * @throws Exception
     */
    byte[] getCraneInfo(Map<String,Object> map) throws Exception;

    /**
     * 获取报警信息给智慧工地
     * @param map
     * @return
     * @throws Exception
     */
    byte[] getAlarmInfo(Map<String,Object> map) throws Exception;

    /**
     * 获取报警详情给智慧工地
     * @param map
     * @return
     * @throws Exception
     */
    byte[] getAlarmDetail(Map<String,Object> map) throws Exception;

    /**
     * 获取监控状态
     * @param page
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectMonitorStatus(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception;

    /**
     * 获取运行时间
     * @param page
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectRunTime(Page<RealTimeMonitoringTowerVo> page,RequestDTO<RealTimeMonitoringTowerVo> requestDTO) throws Exception;

    /**
     *  近期数据
     * @param deviceNo
     * @param uuid
     * @return
     */
    CurrentCraneDataVO getMonitorData(String deviceNo, String uuid);
}
