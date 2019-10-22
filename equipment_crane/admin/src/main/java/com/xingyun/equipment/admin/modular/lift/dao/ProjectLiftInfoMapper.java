package com.xingyun.equipment.admin.modular.lift.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectLiftDetail;
import com.xingyun.equipment.admin.modular.device.vo.ElectricAlarmVO;
import com.xingyun.equipment.admin.modular.device.vo.ProjectLiftAlarmVO;
import com.xingyun.equipment.admin.modular.lift.model.ProjectLiftAlarmInfo;
import com.xingyun.equipment.admin.modular.lift.model.ProjectLiftInfo;
import com.xingyun.equipment.admin.modular.lift.vo.ProjectLiftListVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-26
 */
public interface ProjectLiftInfoMapper  {
   /**
    * 升降机列表
    * @param map
    * @return
    */
	List<ProjectLiftInfo> getLiftInfo (Map<String,Object> map);
	 /**
	    * 升降机列表
	    * @param map
	    * @return
	    */
		Integer getLiftInfoCount (Map<String,Object> map);
		/**
		    * 升降机报警列表
		    * @param map
		    * @return
		    */
			Integer getAlarmInfoCount (Map<String,Object> map);
	/**
	 * 升降机报警
	 * @param map
	 * @return
	 */
	List<ProjectLiftAlarmInfo> getAlarmInfo (Map<String,Object> map);
	/**
	 * 升降机报警
	 * @param map
	 * @return
	 */
	List<ProjectLiftAlarmInfo> getAlarmDetail (Page<ProjectLiftAlarmInfo> page,Map<String,Object> map);
    /**
     * 查询集团下所有升降机接口)
     * @param request
     * @return
     */
    List<ProjectLiftListVO> getLiftList(RequestDTO request);
    /**
     * 查询升降机最近一条数据(接口)
     * @param map
     * @return
     */
    ProjectLiftListVO getLiftDetail(Map<String,Object> map);
    /**
     * 查询升降机当月重量报警/预警 数量
     * @param map
     * @return
     */
    List<ProjectLiftAlarmVO> getLiftAlarmCount(Map<String,Object> map);

	/**
	 * 有数值的数据
	 * @param uuid
	 * @param deviceNo
	 * @author: wangyifei
	 * @return
	 */
	@Select("SELECT height as height ,weight as weight ," +
			"speed as speed,people as people ," +
			"front_door_status as frontDoorStatus ," +
			"back_door_status as backDoorStatus ," +
			"tilt_angle as tiltAngle ," +
			"floor as floor,floor_start as floorStart," +
			"floor_end as floorEnd " +
			" FROM `t_project_lift_detail_201809`  where weight>0 and height >0 and device_no=#{deviceNo}")
    List<ProjectLiftDetail> getTop100LiftDetail(@Param("uuid") String uuid, @Param("deviceNo") String deviceNo);

}
