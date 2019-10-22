package com.xingyun.equipment.crane.modular.device.service;

/**
*Description:
*Company:星云网格
*@author zhouyujie
*@date 2018年11月22日 
*/
public interface DeviceVideoService {

	/**
	 * 对外接口，视频一览，萤石云设备用
	 * @param res
	 * @return
	 */
	String getYSYCameraInfo(String appKey, String secret, Integer pageSize, Integer pageIndex);
}
