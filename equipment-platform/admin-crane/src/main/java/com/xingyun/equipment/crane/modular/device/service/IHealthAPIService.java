package com.xingyun.equipment.crane.modular.device.service;

import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.dto.HealthAPI1DTO;
import com.xingyun.equipment.crane.modular.device.dto.HealthAPI3DTO;
import com.xingyun.equipment.crane.modular.device.dto.HealthAPI4DTO;
import com.xingyun.equipment.crane.modular.device.dto.ProjectInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
     ResultDTO<HealthAPI1DTO> getUserinfo(String uuid, String code);

    /**
     *
     * @param uuid
     * @return
     */
    ResultDTO<HealthAPI3DTO> getAlarminfo(String uuid);

    /**
     *
     * @param uuid
     * @return
     */
    ResultDTO<ProjectInfoDTO> getProjectinfo(String uuid);

    /**
     *
     * @param uuid
     * @return
     */
    ResultDTO<List<HealthAPI4DTO>> userlocation(String uuid);


}
