package com.xywg.equipmentmonitor.modular.device.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.device.dto.CountAlarmByDeviceNo;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitorAlarm;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitorDetail;
import com.xywg.equipmentmonitor.modular.device.vo.AlarmInfoVO;
import com.xywg.equipmentmonitor.modular.device.vo.DeviceUUIDVO;
import com.xywg.equipmentmonitor.modular.station.vo.ProjectEnvironmentMonitorAlarmVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.security.access.method.P;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
public interface ProjectEnvironmentMonitorAlarmMapper extends BaseMapper<ProjectEnvironmentMonitorAlarm> {

    List<ProjectEnvironmentMonitorAlarm> selectPageList(RowBounds rowBounds,
                                                   @Param("ew") Wrapper<RequestDTO<ProjectEnvironmentMonitorAlarm>> wrapper);

    /**
     * 根据时间查询报警明细(不带分页)
     * @param countAlarmByDeviceNo
     * @return
     */
    List<ProjectEnvironmentMonitorAlarm> getAlarmByDateTime (@Param("alarm") CountAlarmByDeviceNo countAlarmByDeviceNo);


    /**
     * 根据时间查询报警明细(带分页)
     * @param countAlarmByDeviceNo
     * @return
     */
    List<ProjectEnvironmentMonitorAlarm> getPageAlarmByDateTime (Page<ProjectEnvironmentMonitorAlarm> page,@Param("alarm") CountAlarmByDeviceNo countAlarmByDeviceNo);

    /**
     *
     * @param tableName
     * @param id
     * @param userId
     * @param userName
     * @return
     */
    int updateAlarm(@Param("tableName") String tableName,@Param("id") Integer id,@Param("userId") Integer userId, @Param("userName")String userName);

    /**
     *
     * @return
     */
    DeviceUUIDVO selectDeviceUUID(@Param("deviceTable") String deviceTable   ,@Param("alarmTable")String alarmTable, @Param("id") Integer id);

    /**
     *
     * @param tableName
     * @param id
     * @return
     */
    String selectModifyUserName(@Param("tableName") String tableName,@Param("id") Integer id);

    /**
     *
     * @param rowBounds
     * @param month
     * @param monitorIds
     * @param liftIds
     * @param craneIds
     * @return
     */
    List<AlarmInfoVO> getAlarmInfo(RowBounds rowBounds  ,@Param("month") String month,@Param("monitorIds") Integer[] monitorIds,@Param("liftIds") Integer[] liftIds,@Param("craneIds") Integer[] craneIds);

    /**
     *
     * @param month
     * @param monitorIds
     * @param liftIds
     * @param craneIds
     * @return
     */
    Integer getAlarmAccount(@Param("month") String month,@Param("monitorIds") Integer[] monitorIds,@Param("liftIds") Integer[] liftIds,@Param("craneIds") Integer[] craneIds);

    /**
     *
     * @param month
     * @return
     */
    List<AlarmInfoVO> getAlarmInfoByFlag(RowBounds rowBounds  ,@Param("flag") int flag, @Param("month") String month,@Param("ids") Integer[] ids);


    /**
     *
     * @param ids
     * @return
     */
    List<Map<String,Object>>  getMonitorDetails(@Param("ids") List<Integer> ids,@Param("month") String month);

    List<Map<String,Object>>  getLiftDetails(@Param("ids") List<Integer> ids,@Param("month") String month);

    List<Map<String,Object>>  getCraneDetails(@Param("ids") List<Integer> ids,@Param("month") String month);




    /**
     *  给智慧工地的
     * @param month
     * @param projectId
     * @return
     */
    List<ProjectEnvironmentMonitorAlarmVO> getAlarms2zhgd(@Param("month") String month, @Param("projectId") Integer projectId, @Param("time") String time);

    /**
     * 银江国际塔吊报警
     * @param month
     * @param projectId
     * @param time
     * @param deviceNo
     * @param status
     * @return
     */
    List<ProjectEnvironmentMonitorAlarmVO> getAlarms2yjgj(@Param("month") String month, @Param("projectId") Integer projectId, @Param("time") String time, @Param("deviceNo") String deviceNo, @Param("status")Integer status);

    List<ProjectEnvironmentMonitorAlarmVO> getLiftAlarms2zhgd(@Param("month") String month, @Param("projectId") Integer projectId, @Param("time") String time, @Param("deviceNo") String deviceNo, @Param("status")Integer status);

}
