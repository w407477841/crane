package com.xingyun.equipment.crane.modular.infromation.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectLift;
import com.xingyun.equipment.crane.modular.infromation.model.ProjectTargetSetLift;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xss
 * @since 2018-08-21
 */
@Service
public interface ProjectTargetSetLiftService extends IService<ProjectTargetSetLift> {

    /**
     * 条件分页查询
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectTargetSetLift>>> selectPageList(RequestDTO<ProjectTargetSetLift> request);

    /**
     * 判重
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectTargetSetLift>>> checkBySpecificationAndManufactor(RequestDTO<ProjectTargetSetLift> request);

    /**
     * 占用
     * @param map
     * @return
     * @throws Exception
     */
    Integer plusCallTimes(Map<String, Object> map) throws Exception;

    /**
     * 解除占用
     * @param projectLift
     * @return
     * @throws Exception
     */
    Integer minusCallTimes(List<ProjectLift> projectLift);
}
