package com.xywg.equipmentmonitor.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.vo.TrendItemVO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCraneDetail;
import com.xywg.equipmentmonitor.modular.station.dto.WeightAndMomentVO;
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
     * @param id
     * @param columnName
     * @param type
     * @param beginDate
     * @param endDate
     * @return
     */
    ResultDTO<DataDTO<List<Map<String,Object>>>> changeToChart(Integer id,String columnName,Integer type,String beginDate,String endDate);

    /**
     *获取实力吊重和力矩数据
     * @param param
     * @return
     */
    List<WeightAndMomentVO> getWeightAndMomentVO(Map<String,Object> param);
}
