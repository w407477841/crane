package com.xywg.equipmentmonitor.modular.device.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.common.constant.Const;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.model.ProjectElectricPower;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDeviceStock;
import com.xywg.equipmentmonitor.modular.station.service.impl.ProjectDeviceStockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectEnvironmentMonitorDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.device.service.ProjectEnvironmentMonitorService;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@RestController
@RequestMapping(value = "/ssdevice/device/projectEnvironmentMonitor")
public class ProjectEnvironmentMonitorController
		extends BaseController<ProjectEnvironmentMonitor, ProjectEnvironmentMonitorService> {

	@Autowired
	private ProjectDeviceStockServiceImpl projectDeviceStockService;

	@Override
	public String insertRole() {
		return null;
	}

	@Override
	public String updateRole() {
		return null;
	}

	@Override
	public String deleteRole() {
		return null;
	}

	@Override
	public String viewRole() {
		return null;
	}

	/**
	 * 绿色施工监控一览
	 * 
	 * @param res
	 * @return
	 */
	@ApiOperation("绿色施工监控一览")
	@PostMapping("selectPageList")
	ResultDTO<DataDTO<List<ProjectEnvironmentMonitor>>> selectPageList(@RequestBody RequestDTO<ProjectEnvironmentMonitor> res) {
		return service.selectPageList(res);
	}
	
	/**
	 * 扬尘设备地图用
	 * 
	 * @param res
	 * @return
	 */
	@ApiOperation("扬尘设备地图用")
	@PostMapping("selectListMap")
	ResultDTO<List<ProjectEnvironmentMonitor>> selectListMap(@RequestBody RequestDTO<ProjectEnvironmentMonitor> res) {
		return service.selectListMap(res);
	}

	/**
	 * 绿色施工监控合计
	 * 
	 * @param res
	 * @return
	 */
	@ApiOperation("绿色施工监控设备合计")
	@PostMapping("countDevice")
	public ResultDTO<Object> countDevice(@RequestBody RequestDTO<ProjectEnvironmentMonitor> res) {
		return service.countDevice(res.getBody().getDeviceNo(),res.getBody().getProjectId());
	}

	/**
	 * 单条
	 * 
	 * @param res
	 * @return
	 */
	@ApiOperation("绿色施工监控单条")
	@PostMapping("selectInfo")
	public ResultDTO<ProjectEnvironmentMonitorDTO> selectInfo(@RequestBody RequestDTO<ProjectEnvironmentMonitor> res) {
		return service.selectInfo(res);
	}
	/**
	 * 新增
	 * @param res
	 * @return
	 */
	@ApiOperation("新增")
	@PostMapping("insertInfo")
	public ResultDTO<Object> insertInfo(@RequestBody RequestDTO<ProjectEnvironmentMonitorDTO> res){
		try {
			return service.insertInfo(res);
		} catch (Exception e) {
			return new ResultDTO<>(false);
		}
		
	}
	
	/**
	 * 启用
	 * @param res
	 * @return
	 */
	@ApiOperation("启用")
	@PostMapping("updateStatus")
	public ResultDTO<Object> updateStatus(@RequestBody RequestDTO<ProjectEnvironmentMonitor> res) {
		try {
			return service.updateStatus(res);
		} catch (Exception e) {
			return new ResultDTO<>(false);
		}
	}
	
	/**
	 * 编辑
	 * @param res
	 * @return
	 */
	@ApiOperation("编辑")
	@PostMapping("updateInfo")
	public ResultDTO<Object> updateInfo(@RequestBody RequestDTO<ProjectEnvironmentMonitorDTO> res){
		try {
			return service.updateInfo(res);
		} catch (Exception e) {
			return new ResultDTO<>(false);
		}
		
	}

	@ApiOperation("获取部门下所有扬尘设备")
	@GetMapping(value = "/getByOrgId")
	public ResultDTO<Map<String,Object>> getByOrgId(@RequestParam Integer orgId) {
		try {
			List<ProjectEnvironmentMonitor> list = service.selectByOrgId(Const.orgIds.get());
			Map<String,Object> map = new HashMap<>(10);
			map.put("environmentList",list);
			return new ResultDTO<>(true,map);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}

	@ApiOperation("获取部门下扬尘设备数量")
	@GetMapping(value = "/getCountByOrgId")
	public ResultDTO<Map<String,Object>> getCountByOrgId(@RequestParam Integer orgId) {
		try {
			Wrapper<ProjectEnvironmentMonitor> wrapper = new EntityWrapper<>();
			wrapper.in("org_id", Const.orgIds.get());
			Map<String,Object> map = new HashMap<>(10);
			map.put("amount",service.selectCount(wrapper));
			return new ResultDTO<>(true,map);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}

	/**
	 * 数据大屏页面接口
	 * @param deviceNo
	 * @return
	 */
	@GetMapping("/getEnvironmentInfoForScreen")
	public Map<String,Object> getEnvironmentInfoForScreen(@RequestParam String deviceNo) {
		Map<String,Object> result = new HashMap<>();
		try {
			result.put("data",service.getEnvironmentInfoForScreen(deviceNo));
			return result;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@PostMapping("deletes")
	@Override
	public ResultDTO<ProjectEnvironmentMonitor>  deletes(@RequestBody  RequestDTO<ProjectEnvironmentMonitor> t){
		hasPermission(deleteRole());
		try {
			List<ProjectDeviceStock> list= new ArrayList<>(1);
			for(int i=0;i<t.getIds().size();i++){
				Integer id= (Integer) t.getIds().get(i);
				EntityWrapper<ProjectEnvironmentMonitor> wrapper =new EntityWrapper<>();
				wrapper.eq("id",id);
				ProjectEnvironmentMonitor monitor=  service.selectOne(wrapper);
				if(monitor !=null){
					Map<String, Object> map = new HashMap<>();
					map.put("device_no",monitor.getDeviceNo());
					map.put("is_del",0);
					List<ProjectDeviceStock> stocks= projectDeviceStockService.selectByMap(map);
					if(stocks.size()>0){
						ProjectDeviceStock stock = stocks.get(0);
						stock.setStatus(0);
						list.add(stock);
					}

				}

			}
			if(service.deleteBatchIds(t.getIds())){
				if(list.size()>0){
					projectDeviceStockService.updateAllColumnBatchById(list);
					return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDTO.resultFactory(OperationEnum.DELETE_ERROR);
		}
		return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
	}

	/**
	 * 转发
	 * @param res
	 * @return
	 */
	@ApiOperation("转发")
	@PostMapping("updateDispatch")
	public ResultDTO<Object> updateDispatch(@RequestBody RequestDTO<ProjectEnvironmentMonitor> res) {
		try {
			return service.updateDispatch(res);
		} catch (Exception e) {
			return new ResultDTO<>(false);
		}
	}
}
