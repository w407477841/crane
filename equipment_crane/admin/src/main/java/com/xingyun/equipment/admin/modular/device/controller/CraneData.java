package com.xingyun.equipment.admin.modular.device.controller;
import com.xingyun.equipment.admin.core.dto.AlarmInfoDTO;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectCraneAlarm;
import com.xingyun.equipment.admin.modular.device.service.*;
import com.xingyun.equipment.admin.modular.device.service.impl.CraneDataServiceImpl;
import com.xingyun.equipment.admin.modular.device.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
@RestController
@RequestMapping(value = "/ssdevice/craneData/api")
public class CraneData {

    @Value("${filePath}")
    private String filePath;
    @Autowired
    private CraneDataService craneDataService;
    @Autowired
    private IProjectCraneAlarmService iProjectCraneAlarmService;
    /**
     * 设备流量统计
     *
     * @param requestDTO 请求参数
     * @return 返回的页面信息
     */
    @PostMapping(value = "/getDeviceFlow")
    public ResultDTO<DataDTO<List<CraneFlowVO>>> selectDeviceFlow(@RequestBody RequestDTO<RequestCraneFlowVO> requestDTO)
    {
        return craneDataService.selectDeviceFlow(requestDTO);
    }

    /**
     * 导出设备流量统计Excel接口
     *
     * @param response  返回的响应信息
     * @param projectId 工程编号
     * @param gprs      SIM卡号
     * @return 返回的页面信息
     */
    @GetMapping(value = "/getDeviceFlowExcel")
    public ResultDTO getDeviceFlowExcel(HttpServletResponse response, Integer projectId, String gprs,String keyWord)
    {
        return craneDataService.getDeviceFlowExcel(response,projectId,gprs,keyWord);
    }


    /**
     * 设备离线时长统计接口
     * @param requestDTO 请求信息
     * @return 返回的页面请求数据
     */
    @PostMapping(value = "/deviceOffline")
    public ResultDTO<DataDTO<List<DeviceOfflineVO>>> selectDeviceOffline(@RequestBody RequestDTO<DeviceOfflineVO> requestDTO)
    {
        return craneDataService.selectDeviceOffline(requestDTO);
    }


    /**
     * 设备离线时长导出Excel接口
     * @param response  返回的响应信息
     * @param projectId 工程编号
     * @return 返回的页面请求数据
     */
    @GetMapping(value = "/deviceOfflineExcel")
    public ResultDTO<DataDTO<List<DeviceOfflineVO>>> getDeviceOfflineExcel(HttpServletResponse response, Integer projectId,String keyWord)
    {
        return craneDataService.selectDeviceOfflineExcel(response,projectId,keyWord);
    }

    /**
     * 设备循环工作时长统计接口
     *
     * @param requestDTO 请求参数
     * @return 返回的页面请求数据
     */
    @PostMapping(value = "/getMomentPercent")
    public ResultDTO<DataDTO<List<WeightPercentVO>>> selectMomentPercent(@RequestBody RequestDTO<WeightPercentVO> requestDTO)
    {
        return craneDataService.selectMomentPercent(requestDTO);
    }

    /**
     * 导出设备循环工作时长Excel接口
     *
     * @param projectId 工程编号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param response  响应信息
     */
    @GetMapping(value = "/getMomentPercentExcel")
    public void getMomentPercentExcel(Integer projectId, String startTime, String endTime, HttpServletResponse response)
    {
        craneDataService.getMomentPercentExcel(projectId,startTime,endTime,response);
    }



    /**
     * //力矩百分比查看接口
     * @param requestDTO 请求数据
     * @return 响应信息
     */
    @PostMapping(value = "/getMomentInfo")
    public ResultDTO<WeightPercentVO> selectMomentInfo(@RequestBody RequestDTO<WeightPercentVO> requestDTO)
    {
        return craneDataService.selectMomentInfo(requestDTO);
    }


    /**
     * 力矩百分比详情接口
     *
     * @param requestDTO
     * @return
     */
    @PostMapping(value = "/getMomentPercentInfo")
    public ResultDTO<DataDTO<List<MomentPercentInfo>>> selectMomentPercentInfo(@RequestBody RequestDTO<WeightPercentVO> requestDTO)
    {
        return craneDataService.selectMomentPercentInfo(requestDTO);
    }

    /**
     * 力矩百分比过滤导出Excel接口
     * @param requestDTO
     * @param response
     */
    @GetMapping(value = "/selectMomentPercentInfoExcel")
    public void getMomentPercentInfoExcel(@RequestBody RequestDTO<WeightPercentVO> requestDTO,HttpServletResponse response){
        craneDataService.selectMomentPercentInfoExcel(requestDTO,response);
    }

    /**
     * 预警信息一览，报警信息一览，违章信息一览查询接口
     *
     * @param requestDTO 请求参数
     * @return 响应信息
     */
    @PostMapping(value = "/getWarnInfo")
    public ResultDTO<DataDTO<List<GetInfoVO>>> selectWarnInfo(@RequestBody RequestDTO<GetInfoVO> requestDTO)
    {
        try {
            return craneDataService.selectWarnInfo(requestDTO);
        }catch (Exception e)
        {
            return null;
        }

    }
    /**
     * 预警信息一览，报警信息一览，违章信息一览导出Excel接口
     * @param type 请求类型
     * @param deviceNo 黑匣子编号
     * @param projectId 工程编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param keyWord 塔吊编号/黑匣子编号
     * @param response 响应信息
     */
    @GetMapping(value="getWarnInfoExcel")
    public void getWarnInfoExcel(Integer type,String deviceNo,Integer projectId,String startTime,String endTime,String keyWord,HttpServletResponse response)
    {
        try {
            craneDataService.getWarnInfoExcel(type,deviceNo,projectId,startTime,endTime,keyWord,response);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 预警信息一览，报警信息一览，违章信息一览详情
     *
     * @param requestDTO
     * @return
     */
    @PostMapping(value = "/selectAlarmInfo")
    public ResultDTO<DataDTO<List<AlarmInfoDTO>>> selectAlarmInfo(@RequestBody RequestDTO<AlarmInfoVO> requestDTO)
    {
        return craneDataService.selectAlarmInfo(requestDTO);
    }

    /**
     * @param requestDTO 请求参数
     * @return 工作等级统计接口
     */
    @PostMapping(value = "/selectWorkGrade")
    public ResultDTO<DataDTO<List<WorkGradeVO>>> selectWorkGrade(@RequestBody RequestDTO<WorkGradeVO> requestDTO)
    {
        return craneDataService.selectWorkGrade(requestDTO);
    }

    /**
     * 设备在线统计
     * @param requestDTO 请求参数
     * @return 返回页面信息
     */
    @PostMapping(value = "/selectIsOnline")
    public ResultDTO<List<IsOnlineVO>> selectIsOnline(@RequestBody RequestDTO<IsOnlineVO> requestDTO)
    {
        try {
            return craneDataService.selectIsOnline(requestDTO);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 导出设备在线统计Excel接口
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 1：按设备 2 按项目
     * @param response 响应信息
     */
    @GetMapping(value = "/selectIsOnlineExcel")
    public void getIsOnlineExcel(String startTime,String endTime,int type, HttpServletResponse response){
        try{
        craneDataService.selectIsOnlineExcel(startTime,endTime,type,response);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }



    /**
     * 处理
     * @param alarm
     * @return
     */
    @PostMapping(value="updateHandle")
    public ResultDTO<Object> updateHandle(@RequestBody RequestDTO<ProjectCraneAlarm> alarm)
    {
        try {
            return iProjectCraneAlarmService.updateHandle(alarm.getBody());
        } catch (Exception e) {
            return new ResultDTO<>(false);
        }
    }

    /**
     * 预警信息一览，报警信息一览，违章信息一览详情导出Excel
     *
     * @param requestDTO 请求参数
     * @return 响应信息
     */
    @GetMapping(value="selectAlarmInfoExcel")
    public void getAlarmInfoExcel(@RequestBody RequestDTO<AlarmInfoVO> requestDTO,HttpServletResponse response)
    {
        craneDataService.selectAlarmInfoExcel(requestDTO,response);
    }

    /**
     * 工作等级统计导出接口
     *
     * @param projectId 工程编号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param keyWord   塔吊编号/黑匣子编号
     * @param response  响应信息
     */
    @GetMapping(value="getWorkGradeExcel")
    public void getWorkGradeExcel(Integer projectId,String startTime,String endTime,String keyWord, HttpServletResponse response)
    {
        craneDataService.getWorkGradeExcel(projectId,startTime,endTime,keyWord, response);
    }

    @PostMapping(value="towerIndex")
    public ResultDTO<DataDTO<List<TowerIndexVO>>> towerIndex(@RequestBody RequestDTO<TowerIndexVO> requestDTO) {
    return craneDataService.towerIndex(requestDTO);
    }


}
