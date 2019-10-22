package com.xywg.equipmentmonitor.modular.device.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.common.constant.FlagEnum;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.RedisUtil;
import com.xywg.equipmentmonitor.core.util.StringCompress;
import com.xywg.equipmentmonitor.modular.device.dto.DeviceStatus;
import com.xywg.equipmentmonitor.modular.device.dto.OnlineDTO;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorService;
import com.xywg.equipmentmonitor.modular.projectmanagement.model.ProjectInfo;
import com.xywg.equipmentmonitor.modular.projectmanagement.service.IProjectInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitorDetail;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorDetailService;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Description:
 * Company:星云网格
 *
 * @author zhouyujie
 * @date 2018年8月21日
 */
@RestController
@RequestMapping("ssdevice/project/projectEnvironmentMonitorDetail")
public class ProjectEnvironmentMonitorDetailController extends BaseController<ProjectEnvironmentMonitorDetail, ProjectEnvironmentMonitorDetailService> {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IProjectInfoService  projectInfoService;
    @Autowired
    private ProjectEnvironmentMonitorService   projectEnvironmentMonitorService;

    @Override
    public String insertRole() {
        return null;
    }

    @Override
    public String updateRole() {
        return null;
    }

    @Override
    public String deleteRole() {
        return null;
    }

    @Override
    public String viewRole() {
        return null;
    }

    /**
     * 绿色施工监控一览
     * 有分表
     *
     * @param res
     * @return
     */
    @ApiOperation("绿色施工监控一览明细")
    @PostMapping("selectPageList")
    ResultDTO<DataDTO<List<ProjectEnvironmentMonitorDetail>>> selectPageList(@RequestBody RequestDTO<ProjectEnvironmentMonitorDetail> res) {
        return service.selectPageList(res);
    }

    /**
     *
     * @param param  参数map
     * @return
     */
    @ApiOperation("7小时趋势")
    @GetMapping("trend")
    Map<String, Object> getTrend(@RequestParam Map<String, Object> param) {
        String uuid = (String) param.get("uuid");
        String deviceNo = (String) param.get("deviceNo");
        String columnName = (String) param.get("columnName");
        String startTime = (String) param.get("startTime");
        String endTime = (String) param.get("endTime");
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("flag", FlagEnum.getFlag(columnName));
        if(StrUtil.isBlank(startTime)){
            if ("wind_direction".equals(columnName)) {
                data.put("trendList", service.windDirectionTrend(uuid, deviceNo));
                result.put("data", data);
                return data;
            } else {
                data.put("trendList", service.trend(uuid, deviceNo, columnName));
                result.put("data", data);
                return data;
            }
        }else{
            if ("wind_direction".equals(columnName)) {
                data.put("trendList", service.windDirectionTrend(uuid, deviceNo,"",startTime,endTime));
                result.put("data", data);
                return data;
            } else {
                data.put("trendList", service.trend(uuid, deviceNo, columnName,startTime,endTime));
                result.put("data", data);
                return data;
            }
        }



    }

    @ApiOperation("近7小时趋势")
    @GetMapping("/getTrend")
	public ResultDTO getRecentTrend(@RequestParam Map<String, Object> param) {
        int projectId = Integer.parseInt((String) param.get("projectId"));
        String columnName = (String) param.get("columnName");
        String deviceNo = (String) param.get("deviceNo");
        return new ResultDTO(true,service.getTrend(projectId, deviceNo, columnName));
    }

    @ApiOperation("设备排行前20")
    @GetMapping("/getRank20")
    public ResultDTO getRank20(@RequestParam Map<String, Object> param) {
        return new ResultDTO<>(true, service.getRank20(param));
    }

    @ApiOperation("近期最后一条扬尘数据")
    @GetMapping("getMonitorData")
    Map<String, Object> getMonitorData(@RequestParam Map<String, Object> param) {
        String uuid = (String) param.get("uuid");
        String deviceNo = (String) param.get("deviceNo");
        Map<String, Object> result = new HashMap<>();
        result.put("data", service.getMonitorData(uuid, deviceNo));
        return result;
    }

    @ApiOperation("扬尘设备最近一条数据")
    @GetMapping("/getLastOne")
    public ResultDTO getLastOne(@RequestParam Map<String, Object> param) {
        return new ResultDTO<>(true,service.getLastOne(param));
    }

    @ApiOperation("获取天气信息")
    @GetMapping("getWeatherInfo")
    public ResultDTO<Object> getWeatherInfo(String city) {
        HashMap<String, Object> paramMap = new HashMap<>(16);
        paramMap.put("city", city);
        String result3 = HttpUtil.get("http://wthrcdn.etouch.cn/weather_mini", paramMap);
        return new ResultDTO<>(true, result3);
    }

    @ApiOperation("获取设备状态")
    @PostMapping("getDeviceStatus")
    ResultDTO<Map<String, Object>> getDeviceStatus2(@RequestBody Map<String, Object> map) {
        return getDeviceStatus(((String) map.get("uuids")).split(","));
    }

    @ApiOperation("获取设备状态")
    @GetMapping("getDeviceStatus")
    ResultDTO<Map<String, Object>> getDeviceStatus(@RequestParam(value = "uuids", required = false) String[] uuids) {

        int craneAmount = 0;
        int craneIsOn = 0;
        int craneIsOff = 0;

        int environmentAmount = 0;
        int environmentIsOn = 0;
        int environmentIsOff = 0;

        int liftAmount = 0;
        int liftIsOn = 0;
        int liftIsOff = 0;
        if(uuids!=null&&uuids.length>0){
            Wrapper<ProjectInfo> wrapper =new EntityWrapper();
            wrapper.in(true,"uuid",uuids);
            wrapper.eq("is_del",0);
            List<ProjectInfo> projectInfos =  projectInfoService.selectList(wrapper);

            for (ProjectInfo projectInfo : projectInfos) {
                String uuid = projectInfo.getUuid();
                String key = "device_platform:devicestatus:" + uuid;
                String data = (String) redisUtil.get(key);
                if (StrUtil.isBlank(data)) {

                } else {
                    Map<String, Object> status = JSONUtil.toBean(data, Map.class);
                    Map<String, Object> craneInfo = (Map<String, Object>) status.get("craneInfo");
                    Map<String, Object> environmentInfo = (Map<String, Object>) status.get("environmentInfo");
                    Map<String, Object> liftInfo = (Map<String, Object>) status.get("liftInfo");
                    craneAmount += (int) craneInfo.get("amount");
                    craneIsOn += (int) craneInfo.get("isOn");
                    craneIsOff += (int) craneInfo.get("isOff");

                    environmentAmount += (int) environmentInfo.get("amount");
                    environmentIsOn += (int) environmentInfo.get("isOn");
                    environmentIsOff += (int) environmentInfo.get("isOff");

                    liftAmount += (int) liftInfo.get("amount");
                    liftIsOn += (int) liftInfo.get("isOn");
                    liftIsOff += (int) liftInfo.get("isOff");
                }
            }
        }


        DeviceStatus craneInfo = DeviceStatus.factory(craneAmount, craneIsOn, craneIsOff);
        DeviceStatus environmentInfo = DeviceStatus.factory(environmentAmount, environmentIsOn, environmentIsOff);
        DeviceStatus liftInfo = DeviceStatus.factory(liftAmount, liftIsOn, liftIsOff);

        Map<String, Object> result = new HashMap<>();
        result.put("craneInfo", craneInfo);
        result.put("liftInfo", liftInfo);
        result.put("environmentInfo", environmentInfo);

        return new ResultDTO<>(true, result);
    }
    @ApiOperation("获取下线设备")
    @PostMapping("getOfflines")
    ResultDTO<List<OnlineDTO>> getOfflines2(@RequestBody Map<String, Object> map) {
        return getOfflines(((String) map.get("uuids")).split(","),(String)map.get("type"),(Integer)map.get("online"));
    }


    @ApiOperation("获取下线设备")
    @GetMapping("getOfflines")
    public ResultDTO<List<OnlineDTO>> getOfflines(@RequestParam(value = "uuids", required = false) String[] uuids, @RequestParam(value = "type") String type, @RequestParam(value="online")Integer online){
        return  new ResultDTO<>(true,service.getOfflines(uuids,type,online));
    }

    public static void main(String[] args) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HttpUtil.download("http://192.168.20.160:8090/project/realTimeMonitoringTower/getMonitorData?uuid=1&deviceNo=010917110002&columnName=wind_direction", os, true);
        String resultStr = StringCompress.decompress(os.toByteArray());
        System.out.println(resultStr);
    }

    @ApiOperation("7小时趋势")
    @GetMapping("trendForScreen")
    Map<String, Object> getTrendForScreen(@RequestParam Map<String, Object> param) {
        String projectId = (String) param.get("projectId");
        String deviceNo = (String) param.get("deviceNo");
        String columnName = (String) param.get("columnName");
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("flag", FlagEnum.getFlag(columnName));
        if ("wind_direction".equals(columnName)) {
            data.put("trendList", service.mywindDirectionTrendForScreen(projectId, deviceNo));
            result.put("data", data);
            return data;
        } else {
            data.put("trendList", service.trendForScreen(projectId, deviceNo, columnName));
            result.put("data", data);
            return data;
        }

    }

    @ApiOperation("近期最后一条扬尘数据")
    @GetMapping("getMonitorDataForScreen")
    Map<String, Object> getMonitorDataForScreen(@RequestParam Map<String, Object> param) {
        String projectId = (String) param.get("projectId");
        String deviceNo = (String) param.get("deviceNo");
        Map<String, Object> result = new HashMap<>();
        result.put("data", service.getMonitorData(projectId, deviceNo));
        return result;
    }

    @ApiOperation("设备在线状态")
    @GetMapping("getMonitorStatus")
    ResultDTO getMonitorStatus(@RequestParam String uuid) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", projectEnvironmentMonitorService.getDeviceOnlineStatus(uuid,1));
        return new ResultDTO(true,result);
    }
    @ApiOperation("设备在线状态")
    @GetMapping("getCraneStatus")
    ResultDTO getCraneStatus(@RequestParam String uuid) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", projectEnvironmentMonitorService.getDeviceOnlineStatus(uuid,2));
        return new ResultDTO(true,result);
    }
    @ApiOperation("设备在线状态")
    @GetMapping("getLiftStatus")
    ResultDTO getLiftStatus(@RequestParam String uuid) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", projectEnvironmentMonitorService.getDeviceOnlineStatus(uuid,3));
        return new ResultDTO(true,result);
    }



    @ApiOperation("切换到图表")
    @GetMapping("/changeToChart")
    public ResultDTO<DataDTO<List<Map<String,Object>>>> changeToChart(Integer id,String columnName,Integer type,String beginDate,String endDate){
        return service.changeToChart(id,columnName,type,beginDate,endDate);
    }
    
    @GetMapping("/getMonitorInfo")
    public ResultDTO<Map<String,Object>> getMonitorInfo(String uuid,String deviceNo){
        return service.getMonitorInfo(uuid,deviceNo);
    }
}
