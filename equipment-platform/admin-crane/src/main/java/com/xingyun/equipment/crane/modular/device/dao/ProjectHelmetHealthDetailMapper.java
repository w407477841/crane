package com.xingyun.equipment.crane.modular.device.dao;

import com.xingyun.equipment.crane.modular.device.dto.HealthAPI3DTO;
import com.xingyun.equipment.crane.modular.device.dto.HealthAPI4DTO;
import com.xingyun.equipment.crane.modular.device.dto.HealthInfo;
import com.xingyun.equipment.crane.modular.device.dto.HealthLocation;
import com.xingyun.equipment.crane.modular.device.model.ProjectHelmetHealthDetail;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 健康信息(采集数据) Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-11-23
 */
public interface ProjectHelmetHealthDetailMapper extends BaseMapper<ProjectHelmetHealthDetail> {
    /**
     *  查询坐标数据
     * @param month
     * @param daily
     * @param helmetId
     * @return
     */
     List<HealthLocation> selectLocations(@Param("month") String month, @Param("daily") String daily, @Param("helmetId") int helmetId);

    /**
     *
     * @param month
     * @param daily
     * @param time
     * @param helmetId
     * @return
     */
     HealthInfo selectLastInfo(@Param("month") String month, @Param("daily") String daily, @Param("time") String time, @Param("helmetId") int helmetId);

    /**
     *
     * @param month
     * @param daily
     * @param helmetId
     * @return
     */
     List<ProjectHelmetHealthDetail> selectdetails(@Param("month") String month, @Param("daily") String daily, @Param("helmetId") int helmetId);

    /**
     *
     * @param month
     * @param daily
     * @param helmets
     * @param time
     * @return
     */


     List<HealthAPI3DTO> selectAlarms(@Param("month") String month, @Param("daily") String daily, @Param("time") String time, @Param("helmets") List<Integer> helmets);

    /**
     * pageSize*pageIndex
     * @param id
     * @param beginDate
     * @param endDate
     * @param monthList
     * @return
     */
     List<ProjectHelmetHealthDetail> getDetailsById(@Param("id") Integer id, @Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("monthList") List<String> monthList);

    /**
     *
     * @param month
     * @param time
     * @param uuid
     * @return
     */
     List<HealthAPI4DTO> userlocations(@Param("month") String month, @Param("time") String time, @Param("uuid") String uuid);

}
