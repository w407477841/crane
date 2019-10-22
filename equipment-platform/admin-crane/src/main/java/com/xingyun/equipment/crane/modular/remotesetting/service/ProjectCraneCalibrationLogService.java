package com.xingyun.equipment.crane.modular.remotesetting.service;
import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectCraneCalibrationLog;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xss
 * @since 2018-09-30
 */
@Service
public interface ProjectCraneCalibrationLogService extends IService<ProjectCraneCalibrationLog>{
    /**
     * 条件分页查询
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectCraneCalibrationLog>>> getPageList(RequestDTO<ProjectCraneCalibrationLog> request);


}
