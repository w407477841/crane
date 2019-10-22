package com.xywg.equipmentmonitor.modular.device.service;

import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.HealthAPI3DTO;
import com.xywg.equipmentmonitor.modular.device.dto.HealthAPI4DTO;
import com.xywg.equipmentmonitor.modular.device.dto.HealthInfo;
import com.xywg.equipmentmonitor.modular.device.dto.HealthLocation;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetHealthDetail;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;

/**
 * <p>
 * 健康信息(采集数据) 服务类
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
public interface IProjectHelmetHealthDetailService extends IService<ProjectHelmetHealthDetail> {
    /**
     *  查询当前坐标数据
     * @param helmetId
     * @return
     */
    List<HealthLocation> selectLocations(int helmetId);


    /**
     *  查询当前坐标数据(时间范围内)
     * @param helmetId
     * @return
     */
    List<HealthLocation> selectLocationByTime(@Param("helmetId") int helmetId, @Param("startTime")String startTime,@Param("endTime")String endTime);

    /**
     *  查询10分钟内最后一条健康数据
     * @param helmetId
     * @return
     */
    HealthInfo selectLastInfo(int helmetId);

    /**
     * 每个小时的一条数据 当天
     * @param helmetId
     * @return
     */

   List<ProjectHelmetHealthDetail> selectDetails(int helmetId);

    /**
     *  当月异常
     * @param helmetIds
     * @return
     */
   List<HealthAPI3DTO> selectAlarms(List<Integer> helmetIds);

    /**
     * 查询最近异常
     * @param helmetIds
     * @param startTime
     * @param endTime
     * @return
     */
    List<HealthAPI3DTO> selectAlarmsToWechat(List<Integer> helmetIds,String startTime,String endTime);

    /**
     * 查询详情
     * @param requestDTO
     * @return
     * @throws Exception
     */
    ResultDTO<DataDTO<List<ProjectHelmetHealthDetail>>> getDetailsById(RequestDTO requestDTO) throws Exception;


    /**
     *  当月异常
     * @param uuid
     * @return
     */
    List<HealthAPI4DTO> userlocation(String uuid);

}
