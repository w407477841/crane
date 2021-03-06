package com.xywg.equipmentmonitor.modular.projectmanagement.service;

import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectDeviceDose;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectDeviceInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectDeviceDoseVO;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectDeviceInfoVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjy
 * @since 2018-08-20
 */
public interface ProjectDeviceService {

    /**
     * 当前项目下所有设备详情信息
     *
     * @param projectDeviceInfo
     * @return
     */
    ResultDTO<List<ProjectDeviceInfoVO>> getDeviceInfo(ProjectDeviceInfo projectDeviceInfo);


    ResultDTO<List<ProjectDeviceInfoVO>> getDeviceWaterInfo(ProjectDeviceInfo projectDeviceInfo);



    /**
     * 当前项目下所有设备信息用量
     *
     * @param projectDeviceDose
     * @return
     */
    ResultDTO<List<ProjectDeviceDoseVO>> getInfoDose(ProjectDeviceDose projectDeviceDose);
    ResultDTO<List<ProjectDeviceDoseVO>> getInfoWaterDose(ProjectDeviceDose projectDeviceDose);

    /**
     *  返回图表数据
     * @return
     */
    ResultDTO<Map<String,Object>>  getChart();

    /**
     *
     * @return
     */
    ResultDTO<Map<String,Object>>   getChart1();

}
