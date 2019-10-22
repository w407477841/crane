package com.xingyun.equipment.admin.modular.device.service;
import com.xingyun.equipment.admin.core.dto.AlarmInfoDTO;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
@Service
public interface CraneDataService {
    /**
     * 设备流量统计
     *
     * @param requestDTO
     * @return
     */
    ResultDTO<DataDTO<List<CraneFlowVO>>> selectDeviceFlow(@RequestBody RequestDTO<RequestCraneFlowVO> requestDTO);

    /**
     * 导出设备流量统计Excel接口
     *
     * @param response
     * @param projectId
     * @param gprs
     * @return
     */
    ResultDTO getDeviceFlowExcel(HttpServletResponse response, Integer projectId, String gprs, String keyWord) ;


    /**
     * 设备离线时长统计接口
     *
     * @param requestDTO
     * @return
     */
    ResultDTO<DataDTO<List<DeviceOfflineVO>>> selectDeviceOffline(@RequestBody RequestDTO<DeviceOfflineVO> requestDTO) ;

    /**
     * 导出设备离线时长Excel接口
     *
     * @param response
     * @param projectId
     * @return
     */
    ResultDTO<DataDTO<List<DeviceOfflineVO>>> selectDeviceOfflineExcel(HttpServletResponse response, Integer projectId, String keyWord) ;



    /**
     * 设备循环工作时长统计接口
     *
     * @param requestDTO
     * @return
     */
    ResultDTO<DataDTO<List<WeightPercentVO>>> selectMomentPercent(@RequestBody RequestDTO<WeightPercentVO> requestDTO) ;

    /**
     * 导出设备循环工作时长Excel接口
     *
     * @param projectId
     * @param startTime
     * @param endTime
     * @param response
     */
    void getMomentPercentExcel(Integer projectId, String startTime, String endTime, HttpServletResponse response) ;

    /**
     * //力矩百分比查看接口
     *
     * @param requestDTO
     * @return
     */
    ResultDTO<WeightPercentVO> selectMomentInfo(@RequestBody RequestDTO<WeightPercentVO> requestDTO) ;


    /**
     * 力矩百分比过滤接口
     *
     * @param requestDTO
     * @return
     */
    ResultDTO<DataDTO<List<MomentPercentInfo>>> selectMomentPercentInfo(@RequestBody RequestDTO<WeightPercentVO> requestDTO) ;

    /**
     * 导出力矩百分比过滤Excel接口
     */
    void selectMomentPercentInfoExcel(@RequestBody RequestDTO<WeightPercentVO> requestDTO, HttpServletResponse response) ;

    /**
     * 预警信息一览，报警信息一览，违章信息一览查询接口
     *
     * @param requestDTO
     * @return
     */
    ResultDTO<DataDTO<List<GetInfoVO>>> selectWarnInfo(@RequestBody RequestDTO<GetInfoVO> requestDTO) throws Exception ;

    /**
     * 预警信息一览，报警信息一览，违章信息一览导出Excel接口
     *
     * @param type
     * @param projectId
     * @param startTime
     * @param endTime
     * @param keyWord
     * @param response
     * @throws Exception
     */
    void getWarnInfoExcel(Integer type,String deviceNo, Integer projectId, String startTime, String endTime, String keyWord, HttpServletResponse response) throws Exception ;

    /**
     * 预警信息一览，报警信息一览，违章信息一览详情
     *
     * @param requestDTO
     * @return
     */
    ResultDTO<DataDTO<List<AlarmInfoDTO>>> selectAlarmInfo(@RequestBody RequestDTO<AlarmInfoVO> requestDTO) ;

    /**
     * 预警信息一览，报警信息一览，违章信息一览详情导出Excel
     *
     * @param requestDTO
     * @return
     */
    void selectAlarmInfoExcel(@RequestBody RequestDTO<AlarmInfoVO> requestDTO, HttpServletResponse response) ;

    /**
     * @param requestDTO 请求参数
     * @return 工作等级统计接口
     */
    ResultDTO<DataDTO<List<WorkGradeVO>>> selectWorkGrade(@RequestBody RequestDTO<WorkGradeVO> requestDTO) ;

    /**
     * 工作等级统计导出接口
     *
     * @param projectId
     * @param startTime
     * @param endTime
     * @param keyWord
     * @param response
     */
    void getWorkGradeExcel(Integer projectId, String startTime, String endTime, String keyWord, HttpServletResponse response) ;

    /**
     * 设备在线统计
     *
     * @param requestDTO
     * @return
     */
    ResultDTO<List<IsOnlineVO>> selectIsOnline(@RequestBody RequestDTO<IsOnlineVO> requestDTO) throws Exception ;


    /**
     * 导出设备在线统计Excel接口
     */
    void selectIsOnlineExcel(String startTime, String endTime, Integer type, HttpServletResponse response) throws Exception ;

    /**
     * 设备台账
     *
     * @param requestDTO
     * @return
     */
    ResultDTO<DataDTO<List<GetInfoVO>>> getDeviceAccount(@RequestBody RequestDTO<GetInfoVO> requestDTO) ;


    /**
     * 塔吊首页接口
     *
     * @param requestDTO
     * @return
     */
    ResultDTO<DataDTO<List<TowerIndexVO>>> towerIndex(@RequestBody RequestDTO<TowerIndexVO> requestDTO) ;


}
