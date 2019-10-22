package com.xingyun.equipment.admin.modular.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.vo.CurrentCraneDataVO;
import com.xingyun.equipment.admin.modular.device.vo.OfflineReasonPieVO;
import com.xingyun.equipment.admin.modular.device.vo.RealTimeMonitoringTowerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/***
 *@author:jixiaojun
 *DATE:2018/8/22
 *TIME:11:29
 */
@Mapper
public interface RealTimeMonitoringTowerMapper extends BaseMapper<RealTimeMonitoringTowerVo> {
    /**
     * 获取塔吊信息
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectCrane(Page<RealTimeMonitoringTowerVo> page,Map<String,Object> map) throws Exception;

    /**
     * 获取塔吊详细数据
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectCraneData(Page<RealTimeMonitoringTowerVo> page,Map<String,Object> map) throws Exception;

    /**
     * 获取预警条数
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectPromptingAlarm(Page<RealTimeMonitoringTowerVo> page,Map<String,Object> map) throws Exception;

    /**
     * 获取报警条数
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectWarningAlarm(Page<RealTimeMonitoringTowerVo> page,Map<String,Object> map) throws Exception;

    /**
     * 获取违章条数
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectViolationAlarm(Page<RealTimeMonitoringTowerVo> page,Map<String,Object> map) throws Exception;

    /**
     * 获取信息详情
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectAlarmData(Page<RealTimeMonitoringTowerVo> page,Map<String,Object> map) throws Exception;

    /**
     * 发送短信
     * @param realTimeMonitoringTowerVo
     * @return
     * @throws Exception
     */
    Integer insertMessage(RealTimeMonitoringTowerVo realTimeMonitoringTowerVo) throws Exception;

    /**
     * 获取塔吊信息给智慧工地
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> getCraneInfo(Page<RealTimeMonitoringTowerVo> page,Map<String,Object> map) throws Exception;

    /**
     * 获取报警信息给智慧工地
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> getAlarmInfo(Page<RealTimeMonitoringTowerVo> page,Map<String,Object> map) throws Exception;

    /**
     * 获取报警详情给智慧工地
     * @param map
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> getAlarmDetail(Page<RealTimeMonitoringTowerVo> page,Map<String,Object> map) throws Exception;

    /**
     * 获取监控状态
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectMonitorStatus(Page<RealTimeMonitoringTowerVo> page,Map<String,Object> map) throws Exception;

    /**
     * 获取运行时间
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> selectRunTime(Page<RealTimeMonitoringTowerVo> page,Map<String,Object> map) throws Exception;


    List<OfflineReasonPieVO>  selectOfflineReasonPie(Map<String,Object> map);
    /**
     * 7小时实时数据
     * @param param
     * @return
     */
    CurrentCraneDataVO getMonitorData(Map<String,Object> param);

    /**
     * 获取运行时间详情
     * @param map
     * @return
     * @throws Exception
     */
    List<RealTimeMonitoringTowerVo> getRunTimeReal(Map<String,Object> map) throws Exception;

    /**
     *  查询设备的在线状况
     * @param deviceNo
     * @param uuid
     * @return
     */
    @Select("select is_online from t_project_crane where device_no=#{deviceNo} and is_del =0 and project_id = (select id from t_project_info where  uuid = #{uuid} and is_del = 0 ) order by create_time desc limit 0,1 ")
    Integer getDeviceOnline(@Param("deviceNo") String deviceNo,@Param("uuid") String uuid);

}
