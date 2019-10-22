package com.xingyun.equipment.crane.modular.device.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.crane.modular.device.dto.CountAlarmByDeviceNo;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentMonitor;
import com.xingyun.equipment.crane.modular.device.model.ProjectEnvironmentMonitorAlarm;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xingyun.equipment.crane.modular.device.vo.AlarmInfoVO;
import com.xingyun.equipment.crane.modular.device.vo.DeviceUUIDVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.security.access.method.P;

import java.util.List;

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
    List<ProjectEnvironmentMonitorAlarm> getAlarmByDateTime(@Param("alarm") CountAlarmByDeviceNo countAlarmByDeviceNo);


    /**
     * 根据时间查询报警明细(带分页)
     * @param countAlarmByDeviceNo
     * @return
     */
    List<ProjectEnvironmentMonitorAlarm> getPageAlarmByDateTime(Page<ProjectEnvironmentMonitorAlarm> page, @Param("alarm") CountAlarmByDeviceNo countAlarmByDeviceNo);

    /**
     *
     * @param tableName
     * @param id
     * @param userId
     * @param userName
     * @return
     */
    int updateAlarm(@Param("tableName") String tableName, @Param("id") Integer id, @Param("userId") Integer userId, @Param("userName") String userName);

    /**
     *
     * @return
     */
    DeviceUUIDVO selectDeviceUUID(@Param("deviceTable") String deviceTable, @Param("alarmTable") String alarmTable, @Param("id") Integer id);

    /**
     *
     * @param tableName
     * @param id
     * @return
     */
    String selectModifyUserName(@Param("tableName") String tableName, @Param("id") Integer id);

    /**
     *
     * @param month
     * @param uuids
     * @return
     */
    List<AlarmInfoVO> getAlarmInfo(RowBounds rowBounds, @Param("month") String month, @Param("uuids") String[] uuids);


    /**
     *
     * @param month
     * @param uuids
     * @return
     */
    List<AlarmInfoVO> getAlarmInfoByFlag(RowBounds rowBounds, @Param("flag") int flag, @Param("month") String month, @Param("uuids") String[] uuids);



}
