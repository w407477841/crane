package com.xingyun.equipment.admin.modular.device.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.vo.CurrentMonitorDataVO;
import com.xingyun.equipment.admin.core.vo.TrendItemVO;
import com.xingyun.equipment.admin.core.vo.WindDirectionTrendItemVO;
import com.xingyun.equipment.admin.modular.device.dto.OnlineDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectEnvironmentMonitorDetail;
import com.xingyun.equipment.admin.modular.device.vo.DeviceRankVO;
import com.xingyun.equipment.admin.modular.device.vo.TrendVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.security.access.method.P;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
public interface ProjectEnvironmentMonitorDetailMapper extends BaseMapper<ProjectEnvironmentMonitorDetail> {
    /**
     * 获取列表
     * @param rowBounds
     * @param map
     * @return
     */
    List<ProjectEnvironmentMonitorDetail> selectPageList(RowBounds rowBounds, @Param("map") Map<String, Object> map);

    /**
     *  风向分表趋势 数据
     * @param param
     * @return
     */
    List<WindDirectionTrendItemVO>  windDirectionTrend(Map<String,Object> param);

    /**
     *  趋势分表 数据
     * @param param
     * @return
     */
    List<TrendItemVO>  trend(Map<String,Object> param);

    /**
     * 实时分表数据
     * @param param
     * @return
     */

    CurrentMonitorDataVO  getMonitorData(Map<String,Object> param);

    /**
     * 获取最近7小时走势
     * @param param
     * @return
     */
    List<TrendVO> getTrend(Map<String,Object> param);

    /**
     * 获取前20
     * @param orgIds
     * @param months
     * @param columnName
     * @return
     */
    List<DeviceRankVO> getRank20(@Param("orgIds")List<Integer> orgIds, @Param("months") String months,@Param("columnName") String columnName);


    /**
     *  风向分表趋势 数据
     * @param param
     * @return
     */
    List<WindDirectionTrendItemVO>  windDirectionTrendForScreen(Map<String,Object> param);

    /**
     *  趋势分表 数据
     * @param param
     * @return
     */
    List<TrendItemVO>  trendForScreen(Map<String,Object> param);

    /**
     *
     * @param uuids 项目uuid
     * @param deviceTableName 设备表名
     * @param heartbeatTableName  心跳表名
     * @param heartbeatColumnName  心跳表中设备表字段
     * @param online 在线状态
     * @return
     */
    List<OnlineDTO> getOffline(@Param("uuids") String uuids[], @Param("deviceTableName") String deviceTableName, @Param("heartbeatTableName") String heartbeatTableName, @Param("heartbeatColumnName") String heartbeatColumnName, @Param("online") Integer online);

    /**
     *  查询设备的在线状况
     * @param deviceNo
     * @param uuid
     * @return
     */
    @Select("select is_online from t_project_environment_monitor where device_no=#{deviceNo} and is_del =0 and project_id = (select id from t_project_info where  uuid = #{uuid} and is_del = 0 ) order by create_time desc limit 0,1 ")
    Integer getDeviceOnline(@Param("deviceNo") String deviceNo,@Param("uuid") String uuid);




	List<Map<String, Object>> getMonitorInfo(Map<String, Object> map);

    /**
     * 切换图表
     * @param id
     * @param columnName
     * @return
     */
    List<Map<String,Object>> changeToChart(@Param("id")Integer id,@Param("columnName") String columnName,@Param("tableName")String tableName,@Param("tableName1")String tableName1,@Param("step")Integer step,@Param("total")Integer total);
//    List<Map<String,Object>> changeToChart(@Param("id")Integer id,@Param("columnName") String columnName, @Param("beginDate") String beginDate, @Param("endDate") String endDate,@Param("month")String month);

    /**
     * 按期间获取数据形成图表
     */
    List<Map<String,Object>> changeToChartAuto(@Param("id")Integer id,@Param("columnName") String columnName,@Param("tableName")String tableName,@Param("tableName1")String tableName1,@Param("beginDate")String beginDate,@Param("endDate")String endDate,@Param("step")Integer step);


}
