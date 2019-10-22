package com.xywg.equipmentmonitor.modular.projectmanagement.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectDeviceDose;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectDeviceInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.AmmeterVO;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectDeviceDoseVO;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectDeviceInfoVO;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjy
 * @since 2018-08-20
 */
@Mapper
public interface ProjectDeviceInfoMapper {
    /**
     * 当前项目下所有设备详情信息
     * @param projectDeviceInfo
     * @return
     */
    List<ProjectDeviceInfoVO> getDeviceInfo(ProjectDeviceInfo  projectDeviceInfo);


    List<ProjectDeviceInfoVO> getDeviceWaterInfo(ProjectDeviceInfo  projectDeviceInfo);

    /**
     * 当前项目下所有设备信息用量
     *
     * @param projectDeviceDose
     * @return
     */
    ProjectDeviceDoseVO getInfoDose(ProjectDeviceDose projectDeviceDose);

    ProjectDeviceDoseVO getInfoWaterDose(ProjectDeviceDose projectDeviceDose);

    /**
     * 图表数据
     * @return
     */
@Select(" SELECT " +
        " (select device_no from t_project_electric_power where id = electric_id ) deviceNo,current_degree as current,LEFT (create_time, 10) deviceTime " +
        " FROM " +
        " t_project_electric_power_detail_201809 " +
        " WHERE " +
        " id IN ( " +
        " SELECT " +
        " max(id) " +
        " FROM " +
        " t_project_electric_power_detail_201809 " +
        " GROUP BY " +
        " LEFT (create_time, 10) " +
        " ) ")
    List<AmmeterVO>  getCharts();

    /**
     * 当前数据
     * @return
     */
    @Select("select current_degree as current from t_project_electric_power_detail_201809 order by create_time desc limit 1")
    Double  getLasts();


}
