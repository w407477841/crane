package com.xywg.equipmentmonitor.modular.device.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectCraneDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneDetailVO;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneOrgVO;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneVO;

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
     * 转发
     *
     * @param res
     * @return
     */
    ResultDTO<Object> updateDispatch(RequestDTO<ProjectCrane> res);
}
