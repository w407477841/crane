package com.xywg.equipmentmonitor.modular.projectmanagement.service.serviceimpl;

import cn.hutool.core.date.DateUtil;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.service.IProjectWaterDailyService;
import com.xywg.equipmentmonitor.modular.device.service.IProjectWaterMeterService;
import com.xywg.equipmentmonitor.modular.device.service.ProjectElectricPowerService;
import com.xywg.equipmentmonitor.modular.projectmanagement.dao.ProjectDeviceInfoMapper;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectDeviceDose;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectDeviceInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.ProjectDeviceService;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.AmmeterVO;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectDeviceDoseVO;
import com.xywg.equipmentmonitor.modular.projectmanagement.vo.ProjectDeviceInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjy
 * @since 2018-08-20
 */
@Service
@SuppressWarnings("all")
public class ProjectDeviceInfoServiceImpl implements ProjectDeviceService {
    @Autowired
    private ProjectDeviceInfoMapper projectDeviceMapper;
    @Autowired
    private IProjectWaterMeterService waterMeterService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ProjectElectricPowerService electricPowerService;
    @Autowired
    private IProjectWaterDailyService waterDailyService;


    /**
     * @param projectDeviceInfo
     * @return
     */
    @Override
    public ResultDTO<List<ProjectDeviceInfoVO>> getDeviceInfo(ProjectDeviceInfo projectDeviceInfo) {
        if (StringUtils.isBlank(projectDeviceInfo.getUuid())) {
            return new ResultDTO<>(false, null, "参数,uuid不能为空");
        }
        List<ProjectDeviceInfoVO> list = projectDeviceMapper.getDeviceInfo(projectDeviceInfo);
        return new ResultDTO<>(true, list, "请求成功");
    }

    /**
     * @param projectDeviceInfo
     * @return
     */
    @Override
    public ResultDTO<List<ProjectDeviceInfoVO>> getDeviceWaterInfo(ProjectDeviceInfo projectDeviceInfo) {
        if (StringUtils.isBlank(projectDeviceInfo.getUuid())) {
            return new ResultDTO<>(false, null, "参数,uuid不能为空");
        }
        List<ProjectDeviceInfoVO> list = projectDeviceMapper.getDeviceWaterInfo(projectDeviceInfo);
        return new ResultDTO<>(true, list, "请求成功");
    }



    /**
     * 当前时间/给定时间的最后一条数据
     * @param projectDeviceDose
     * @return
     */
    @Override
    public ResultDTO<List<ProjectDeviceDoseVO>> getInfoDose(ProjectDeviceDose projectDeviceDose) {
        if (StringUtils.isBlank(projectDeviceDose.getUuid())
                ) {
            return new ResultDTO<>(false, null, "参数uuid不能为空");
        }

        // 查询时间没有，设置当前日期
        if(StringUtils.isBlank(projectDeviceDose.getQueryTime())){
            projectDeviceDose.setQueryTime( DateUtil.format(new Date(),"yyyy-MM-dd"));
        }
        projectDeviceDose.setFrontQueryTime(DateUtil.format(DateUtil.offsetDay(DateUtil.parse( projectDeviceDose.getQueryTime(),"yyyy-MM-dd"),-1),"yyyy-MM-dd"));
        String tableName = projectDeviceDose.getQueryTime().substring(0, 7).replaceAll("-", "");
        projectDeviceDose.setYearMonth(tableName);


        //设备号没有，查询uuid 下所有设备

        List<ProjectDeviceDoseVO>  deviceDoseVOS  = new ArrayList<>();

        if(StringUtils.isBlank(projectDeviceDose.getDeviceNo())){
            ProjectDeviceInfo  deviceInfo  =  new ProjectDeviceInfo();
            deviceInfo.setUuid(projectDeviceDose.getUuid());
            List<ProjectDeviceInfoVO> list = projectDeviceMapper.getDeviceInfo(deviceInfo);
            for(ProjectDeviceInfoVO deviceInfoVO : list){
                projectDeviceDose.setDeviceNo(deviceInfoVO.getDeviceNo());
                ProjectDeviceDoseVO vo =   projectDeviceMapper.getInfoDose(projectDeviceDose);
                deviceDoseVOS.add(vo);
            }
        }else{
            ProjectDeviceDoseVO vo =   projectDeviceMapper.getInfoDose(projectDeviceDose);
            deviceDoseVOS.add(vo);

        }

        return new ResultDTO<>(true, deviceDoseVOS, "请求成功");


    }

    @Override
    public ResultDTO<List<ProjectDeviceDoseVO>> getInfoWaterDose(ProjectDeviceDose projectDeviceDose) {
        if (StringUtils.isBlank(projectDeviceDose.getUuid())
                ) {
            return new ResultDTO<>(false, null, "参数uuid不能为空");
        }

        // 查询时间没有，设置当前日期
        if(StringUtils.isBlank(projectDeviceDose.getQueryTime())){
            projectDeviceDose.setQueryTime( DateUtil.format(new Date(),"yyyy-MM-dd"));
        }
        //
        projectDeviceDose.setFrontQueryTime(DateUtil.format(DateUtil.offsetDay(DateUtil.parse( projectDeviceDose.getQueryTime(),"yyyy-MM-dd"),-1),"yyyy-MM-dd"));
        String tableName = projectDeviceDose.getQueryTime().substring(0, 7).replaceAll("-", "");
        projectDeviceDose.setYearMonth(tableName);


        //设备号没有，查询uuid 下所有设备

        List<ProjectDeviceDoseVO>  deviceDoseVOS  = new ArrayList<>();

        if(StringUtils.isBlank(projectDeviceDose.getDeviceNo())){
            ProjectDeviceInfo  deviceInfo  =  new ProjectDeviceInfo();
            deviceInfo.setUuid(projectDeviceDose.getUuid());
            List<ProjectDeviceInfoVO> list = projectDeviceMapper.getDeviceWaterInfo(deviceInfo);
            for(ProjectDeviceInfoVO deviceInfoVO : list){
                projectDeviceDose.setDeviceNo(deviceInfoVO.getDeviceNo());
                ProjectDeviceDoseVO vo =   projectDeviceMapper.getInfoWaterDose(projectDeviceDose);
                deviceDoseVOS.add(vo);
            }
        }else{
            ProjectDeviceDoseVO vo =   projectDeviceMapper.getInfoWaterDose(projectDeviceDose);
            deviceDoseVOS.add(vo);

        }

        return new ResultDTO<>(true, deviceDoseVOS, "请求成功");


    }


    @Override
    public ResultDTO<Map<String, Object>> getChart() {
        Map<String,Object> data = new HashMap<>();
        data.put("electricityLift",0);

        data.put("waterLift",0);
        data.put("weigwaterProductionhtInvoice",0);

        double count  =  projectDeviceMapper.getLasts();
        data.put("electricityProduction",count);

        Map<String,Object>  water_lift = new HashMap<>();
        water_lift.put("water_production_date",new ArrayList<>());
        water_lift.put("water_production_count",new ArrayList<>());
        data.put("water_lift",water_lift);

       List<AmmeterVO>  charts =   projectDeviceMapper.getCharts();
       List<String>  dates = new ArrayList<>();
       List<Double>  counts  = new ArrayList<>();
        for(AmmeterVO chart:charts){
            dates.add( chart.getDeviceTime());
            counts.add(chart.getCurrent());
        }

        Map<String,Object>  electricity_lift = new HashMap<>();
        electricity_lift.put("water_production_date",dates);
        electricity_lift.put("water_production_count",counts);

        data.put("electricity_lift",electricity_lift);

        return new ResultDTO(true,data);
    }

    @Override
    public ResultDTO<Map<String, Object>> getChart1() {
        List<AmmeterVO>  charts =   projectDeviceMapper.getCharts();
        List<String>  dates = new ArrayList<>();
        List<Double>  counts  = new ArrayList<>();
        for(AmmeterVO chart:charts){
            dates.add( chart.getDeviceTime());
            counts.add(chart.getCurrent());
        }
        Map<String, Object>  chars = new HashMap<>();


        List<Map<String,Object>> list  =new ArrayList<>();
        Map<String,Object>  electricity_lift = new HashMap<>();
        electricity_lift.put("water_production_date",dates);
        electricity_lift.put("water_production_count",counts);
        electricity_lift.put("name","1#其他");
        electricity_lift.put("countType",2326996);
        list.add(electricity_lift);




        return new ResultDTO(true,list);
    }
}
