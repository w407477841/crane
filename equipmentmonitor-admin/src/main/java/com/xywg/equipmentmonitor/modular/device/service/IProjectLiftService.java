package com.xywg.equipmentmonitor.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectLiftDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLift;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectLiftVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yy
 * @since 2018-08-21
 */
public interface IProjectLiftService extends IService<ProjectLift> {
    /**
     * 列表查询（分页）
     * @param res
     * @return
     * @author yuanyang
     */
    ResultDTO<DataDTO<List<ProjectLiftVO>>> selectPageList(RequestDTO<ProjectLiftVO> res);


    /**
     * 查询单条
     * @param res
     * @return
     * @author null
     */
    ResultDTO<ProjectLiftDTO> selectInfo(RequestDTO<ProjectLiftDTO> res);

    /**
     * 新增升降机
     * @param res
     * @return
     * @throws Exception
     * @author yuanyang
     */
    boolean insertProjectLift(RequestDTO<ProjectLiftDTO> res)  throws Exception;

    /**
     * 修改升降机
     * @param res
     * @return
     * @throws Exception
     * @author yuanyang
     */
    boolean updateProjectLift(RequestDTO<ProjectLiftDTO> res)throws Exception;

    /**
     * 升降机启用
     * @param res
     * @return
     * @throws Exception
     * @author yuanyang
     */
    ResultDTO<Object> updateStatus(RequestDTO<ProjectLiftDTO> res);

    /**
     * 升降机地图用
     * @param res
     * @return
     * @author yuanyang
     */
    ResultDTO<List<ProjectLiftVO>> selectListMap(RequestDTO<ProjectLiftVO> res);

    /**
     * 转发
     *
     * @param res
     * @return
     */
    ResultDTO<Object> updateDispatch(RequestDTO<ProjectLift> res);
}
