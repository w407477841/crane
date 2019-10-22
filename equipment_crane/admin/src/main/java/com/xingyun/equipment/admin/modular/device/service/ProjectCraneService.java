package com.xingyun.equipment.admin.modular.device.service;

import java.util.List;

import com.xingyun.equipment.admin.core.dto.AlarmInfoDTO;
import com.xingyun.equipment.admin.modular.device.dto.ProjectCraneStaticsticsByDateDTO;
import com.xingyun.equipment.admin.modular.device.vo.*;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.dto.ProjectCraneDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectCrane;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xss
 * @since 2018-08-22
 */
@Service
public interface ProjectCraneService extends IService<ProjectCrane> {

    /**
     * 条件分页查询
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectCraneVO>>> getPageList(RequestDTO request);
    /**
     * 查询集团下所有塔吊(接口)
     * @param request
     * @return
     */
    ResultDTO<ProjectCraneOrgVO> selectCraneList(RequestDTO request);
    /**
     * 查询塔吊最近一条数据(接口)
     * @param request
     * @return
     */
    ResultDTO<ProjectCraneDetailVO> selectCraneDetail(RequestDTO request);
    /**
     * 判重
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectCrane>>> checkByDeviceNoAndProjectId(RequestDTO<ProjectCrane> request);
    ResultDTO<DataDTO<List<ProjectCrane>>> checkByCraneNoAndProjectId(RequestDTO<ProjectCrane> request);
    ResultDTO<DataDTO<List<ProjectCrane>>> checkByGprsAndProjectId(RequestDTO<ProjectCrane> request);

    /**
     * 新增
     * @param request
     * @return
     * @throws Exception
     */
    ResultDTO<Object> insertInfo(RequestDTO<ProjectCraneDTO> request)throws Exception;

    /**
     * 查询单条
     * @param request
     * @return
     */
    ResultDTO<ProjectCraneDTO> selectInfo(RequestDTO<ProjectCrane> request);

    /**
     * 更新操作
     * @param request
     * @return
     * @throws Exception
     */
    ResultDTO<Object> updateInfo(RequestDTO<ProjectCraneDTO> request) throws Exception;

    /**
     * 启用操作
     * @param request
     * @return
     */
    ResultDTO<Object> updateStatus(RequestDTO<ProjectCrane> request);

    /**
     * 不分页查询
     * @param res
     * @return
     */
    ResultDTO<List<ProjectCraneVO>> selectListMap(RequestDTO<ProjectCraneVO> res);

    /**
     *  模拟数据
     * @param uuid
     * @param devicNo
     * @return
     */
    ResultDTO<ProjectCraneDetailVO> selectTop100CraneDetails(String uuid,String devicNo);

    /**
     * 分时段统计列表
     * @param requestDTO
     * @return
     */
    ResultDTO<DataDTO<List<ProjectCraneVO>>> getAnalysisList(RequestDTO<ProjectCraneVO> requestDTO);

    /**
     * 日期排序
     * @param requestDTO
     * @return
     */
    ResultDTO<DataDTO<List<ProjectCraneStaticsticsByDateDTO>>> getAnalysisListByDate(RequestDTO<ProjectCraneStaticsticsByDateDTO> requestDTO);

    /**
     * 综合平均数
     * @param requestDTO
     * @return
     */
    ResultDTO<DataDTO<List<ProjectCraneStatisticsByDateVO>>> getAnalysisListAvg(RequestDTO<ProjectCraneStatisticsByDateVO> requestDTO);


    /**
     * 导出台账
     * @param response
     * @param projectId
     */
    void getDeviceAccountExcel(HttpServletResponse response, Integer projectId);

    /**
     * 导出分时段记录
     * @param response
     * @param projectId
     */
    void getAnalysisListExcel(HttpServletResponse response, Integer projectId);

    /**
     * 设备台账
     * @param requestDTO
     * @return
     */
    ResultDTO<DataDTO<List<GetInfoVO>>> getDeviceAccount(RequestDTO<GetInfoVO> requestDTO);

    /**
     * 违章信息已处理未处理
     * @param requestDTO
     * @return
     */
    ResultDTO<AlarmInfoDTO> selectAlarmInfoCount(RequestDTO<AlarmInfoVO> requestDTO);
}
