package com.xingyun.equipment.crane.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.device.model.ProjectCraneDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xss
 * @since 2018-12-20
 */
@Service
public interface ProjectCraneDetailService extends IService<ProjectCraneDetail> {

    /**
     * 切换到图表统计
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<Map<String,Object>>>> changeToChart(Integer id, String columnName, Integer type, String beginDate, String endDate);
}
