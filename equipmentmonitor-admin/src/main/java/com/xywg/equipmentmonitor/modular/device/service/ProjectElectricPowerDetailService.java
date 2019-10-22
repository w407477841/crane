package com.xywg.equipmentmonitor.modular.device.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectElectricPowerDetail;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2018-09-28
 */
public interface ProjectElectricPowerDetailService extends IService<ProjectElectricPowerDetail> {

	ResultDTO<DataDTO<List<ProjectElectricPowerDetail>>> selectPageList(
			RequestDTO<ProjectElectricPowerDetail> res);
	
	 /**
	  * 获取用电统计信息给智慧工地
     * @param map
     * @return
     * @throws Exception
     */
    byte[] getElectricInfo(RequestDTO request) throws Exception;
    
    /**
         * 获取用水统计信息给智慧工地
     * @return
     * @throws Exception
     */
    byte[] getWaterInfo(RequestDTO request) throws Exception;
    

	 /**
	  * 获取用电统计信息给智慧工地
    * @return
    * @throws Exception
    */
   byte[] getElectricInfos(RequestDTO request) throws Exception;
   
   /**
        * 获取用水统计信息给智慧工地
    * @return
    * @throws Exception
    */
   byte[] getWaterInfos(RequestDTO request) throws Exception;

}
