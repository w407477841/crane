package com.xingyun.equipment.admin.modular.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectLift;
import com.xingyun.equipment.admin.modular.device.vo.ProjectLiftVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2018-08-21
 */
public interface ProjectLiftMapper extends BaseMapper<ProjectLift> {
    /**
     * 列表查询
     * @param page
     * @param ew
     * @return
     */
    List<ProjectLiftVO> selectPageList(Page<ProjectLiftVO> page,@Param("ew") EntityWrapper<ProjectLiftVO> ew);

    /**
     * 新增
     * @param body
     * @author yuanyang
     */
    void insertProjectLift(@Param("t")ProjectLift body);


    /**
     * 根据编号查询
     * @param ew
     * @return
     */
    ProjectLift selectByName(@Param("ew")EntityWrapper<RequestDTO> ew);

    /**
     * 升降机电子地图
     * @param ew
     * @return
     * @author yuanyang
     */
    List<ProjectLiftVO> selectListMap(@Param("ew")EntityWrapper<RequestDTO<ProjectLiftVO>> ew);

    /**
     * 判重
     * @param id
     * @param deviceNo
     * @return
     */
    List<ProjectLift> checkByDeviceNo(@Param("id")Integer id,@Param("deviceNo")String deviceNo);
}
