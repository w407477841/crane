package com.xywg.equipmentmonitor.modular.device.service;

import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.HealthAPI1DTO;
import com.xywg.equipmentmonitor.modular.device.dto.HealthAPI3DTO;
import com.xywg.equipmentmonitor.modular.device.dto.HealthAPI4DTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:33 2018/11/23
 * Modified By : wangyifei
 */
public interface IHealthAPIService {
    /**
     *  最近 及 当天
     * @param uuid 项目UUID
     * @param code 身份证
     * @return
     */
     ResultDTO<HealthAPI1DTO> getUserinfo(String uuid , String code);

    /**
     *  最近 及 当天 腾讯
     * @param uuid 项目UUID
     * @param code 身份证
     * @return
     */
    ResultDTO<HealthAPI1DTO> getUserinfoTX(String uuid , String code,String startTime,String endTime);

    /**
     *
     * @param uuid
     * @return
     */
    ResultDTO<HealthAPI3DTO> getAlarminfo(String uuid,String wechat);

    /**
     * 微信端用
     * @param uuid
     * @param wechat
     * @param startTime
     * @param endTime
     * @return
     */
    ResultDTO<HealthAPI3DTO> getAlarminfoToWechat(String uuid,String wechat,String startTime,String endTime);

    /**
     *
     * @param uuid
     * @return
     */
    ResultDTO<ProjectInfoDTO> getProjectinfo(String uuid );

    /**
     *
     * @param uuid
     * @return
     */
    ResultDTO<List<HealthAPI4DTO>> userlocation(String uuid);

    /**
     * 查询微信绑定的项目下所有人的安全帽信息
     * @param uuid
     * @param wechat
     * @return
     */
    ResultDTO<List<Map<String,Object>>> getUserListInfo(String wechat);
}
