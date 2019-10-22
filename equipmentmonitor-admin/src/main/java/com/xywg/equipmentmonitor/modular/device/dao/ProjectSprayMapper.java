package com.xywg.equipmentmonitor.modular.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectSpray;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectSprayVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 10:48
 */
@Mapper
public interface ProjectSprayMapper extends BaseMapper<ProjectSpray>{

    /**
     *
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<ProjectSprayVO> getPageList(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO> wrapper);

    ProjectSprayVO getById(@Param("id")Integer id);

    int checkByDeviceNo(@Param("r")RequestDTO requestDTO);

    /**
     *
     * @param deviceNo 扬尘设备编号
     * @return 扬尘设备编号对应的喷淋编号
     */
    List<ProjectSpray> getSpraysByEnvironDevice(String deviceNo);
}
