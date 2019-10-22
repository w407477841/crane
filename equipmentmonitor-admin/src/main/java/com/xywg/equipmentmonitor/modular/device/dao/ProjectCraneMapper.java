package com.xywg.equipmentmonitor.modular.device.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneDetailVO;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xss
 * @since 2018-08-22
 */
public interface ProjectCraneMapper extends BaseMapper<ProjectCrane> {

    /**
     * 条件分页查询
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<ProjectCraneVO> selectPageList(RowBounds rowBounds, @Param("ew") Wrapper<RequestDTO> wrapper);
    /**
     * 查询集团下所有塔吊(接口)
     * @param request
     * @return
     */
    List<ProjectCraneVO> selectCraneList(RequestDTO request);
    /**
     * 查询塔吊最近一条数据(接口)
     * @param map
     * @return
     */
    ProjectCraneDetailVO selectCraneDetail(Map<String,Object> map);

    /**
     * 判重
     * @param request
     * @return
     */
    int checkByDeviceNoAndProjectId(@Param("t") RequestDTO<ProjectCrane> request);

    /**
     * 删除占用
     * @param specification
     * @param manufactor
     * @return
     */
    int minusCallTimes(@Param("specification")String specification,@Param("manufactor")String manufactor);

    /**
     * 占用
     * @param specification
     * @param manufactor
     * @return
     */
    int plusCallTimes(@Param("specification")String specification,@Param("manufactor")String manufactor);

    /**
     * 地图用
     * @param wrapper
     * @return
     */
    List<ProjectCraneVO> getMapList(@Param("ew") Wrapper<RequestDTO<ProjectCraneVO>> wrapper);

    /**
     * 判重
     * @param id
     * @param deviceNo
     * @return
     */
    List<ProjectCrane> checkByDeviceNo(@Param("id") Integer id,@Param("deviceNo") String deviceNo);

    /**
     * 塔机数据 模拟
     * @param uuid
     * @param deviceNo
     * @return
     */
    @Select("select driver ,weight, height , `range`, rotary_angle as rotaryAngle ,tilt_angle as tiltAngle ,wind_speed as windSpeed from t_project_crane_detail_201809  where device_no = #{deviceNo}")
    List<ProjectCraneDetailVO> selectTop100CraneDetails(@Param("uuid")String uuid,@Param("deviceNo") String deviceNo);

    /**
     * 根据id 更新塔吊转发字段
     * @param ids
     * @return
     */
    int updateDispatch(@Param("ids") List<Integer> ids);
}
