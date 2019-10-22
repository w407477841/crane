package com.xingyun.equipment.admin.modular.device.service;

import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.dto.HealthAPI3DTO;
import com.xingyun.equipment.admin.modular.device.dto.HealthAPI4DTO;
import com.xingyun.equipment.admin.modular.device.dto.HealthInfo;
import com.xingyun.equipment.admin.modular.device.dto.HealthLocation;
import com.xingyun.equipment.admin.modular.device.model.ProjectHelmetHealthDetail;
import com.baomidou.mybatisplus.service.IService;

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
