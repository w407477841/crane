package com.xywg.equipmentmonitor.modular.device.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLift;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDeviceStock;
import com.xywg.equipmentmonitor.modular.station.service.impl.ProjectDeviceStockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectElectricPower;
import com.xywg.equipmentmonitor.modular.device.service.ProjectElectricPowerService;
import com.xywg.equipmentmonitor.modular.device.vo.ElectricAlarmVO;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 电表设备管理
 * </p>
 *
 * @author hy
 * @since 2018-08-21
 */
@RestController
@RequestMapping(value = "/ssdevice/device/projectElectricPower")
public class ProjectElectricPowerController
		extends BaseController<ProjectElectricPower, ProjectElectricPowerService> {


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
	 * 电表设备一览
	 * 
	 * @param res
	 * @return
	 */
	@ApiOperation("电表设备一览")
	@PostMapping("selectPageList")
	ResultDTO<DataDTO<List<ProjectElectricPower>>> selectPageList(@RequestBody RequestDTO<ProjectElectricPower> res) {
		return service.selectPageList(res);
	}
	
	/**
	 * 电表地图用
	 * 
	 * @param res
	 * @return
	 */
	@ApiOperation("电表地图用")
	@PostMapping("selectListMap")
	ResultDTO<List<ProjectElectricPower>> selectListMap(@RequestBody RequestDTO<ProjectElectricPower> res) {
		return service.selectListMap(res);
	}

	/**
	 * 电表设备合计
	 * 
	 * @param res
	 * @return
	 */
	@ApiOperation("电表设备合计")
	@PostMapping("countDevice")
	public ResultDTO<Object> countDevice(@RequestBody RequestDTO<ProjectElectricPower> res) {
		return service.countDevice(res.getBody().getDeviceNo(),res.getBody().getProjectId());
	}

	/**
	 * 单条
	 * 
	 * @param res
	 * @return
	 */
	@ApiOperation("电表单条")
	@PostMapping("selectInfo")
	public ResultDTO<ProjectElectricPower> selectInfo(@RequestBody RequestDTO<ProjectElectricPower> res) {
		return service.selectInfo(res);
	}
	/**
	 * 新增
	 * @param res
	 * @return
	 */
	@ApiOperation("新增")
	@PostMapping("insertInfo")
	public ResultDTO<Object> insertInfo(@RequestBody RequestDTO<ProjectElectricPower> res){
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
	public ResultDTO<Object> updateStatus(@RequestBody RequestDTO<ProjectElectricPower> res) {
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
	public ResultDTO<Object> updateInfo(@RequestBody RequestDTO<ProjectElectricPower> res){
		try {
			return service.updateInfo(res);
		} catch (Exception e) {
			return new ResultDTO<>(false);
		}
		
	}

	@ApiOperation("获取部门下所有电表")
	@GetMapping(value = "/getByOrgId")
	public ResultDTO<Map<String,Object>> getByOrgId(@RequestParam Integer orgId) {
		try {
			List<ProjectElectricPower> list = service.selectByOrgId(orgId);
			Map<String,Object> map = new HashMap<>(10);
			map.put("electricList",list);
			return new ResultDTO<>(true,map);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}

	@ApiOperation("获取部门下电表数量")
	@GetMapping(value = "/getCountByOrgId")
	public ResultDTO<Map<String,Object>> getCountByOrgId(@RequestParam Integer orgId) {
		try {
			Wrapper<ProjectElectricPower> wrapper = new EntityWrapper<>();
			wrapper.eq("org_id",orgId);
			Map<String,Object> map = new HashMap<>(10);
			map.put("amount",service.selectCount(wrapper));
			return new ResultDTO<>(true,map);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}
	
	@ApiOperation("首页电表接口")
	@GetMapping(value = "/getElectricInfo")
	public ResultDTO<Map<String,Object>> getElectricInfo(@RequestParam Integer orgId) {
		try {
			return service.getElectricInfo(orgId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultDTO<>(false);
	}
	  /**
		 * 智慧工地拉取用接口
		 * @param res
		 * @return
		 */
		 @GetMapping("/getElectricDetailInfo")
		    public byte[] getElectricDetailInfo(RequestDTO<ProjectElectricPower> res) {
		        byte[] result = {};
		        try {
		            result =  service.getElectricDetailInfo(res);
		           
		        }catch (Exception e) {
		            e.printStackTrace();
		        }
		        return result;
		    }
		 
		 /**
		  * 智慧工地拉取报警信息
		  * @param res
		  * @return
		  */
		 @GetMapping("/getAlarmInfo")
		    public byte[] getAlarmInfo(RequestDTO<ElectricAlarmVO> res) {
		        byte[] result = {};
		        try {
		            result =  service.getAlarmInfo(res);
		            return result;
		        }catch (Exception e) {
		            e.printStackTrace();
		        }
		        return result;
		    }

		 /**
		  * 智慧工地拉取报警信息明细
		  * @param res
		  * @return
		  */
		 @GetMapping("/getAlarmDetail")
		    public byte[] getAlarmDetail(RequestDTO<ElectricAlarmVO> res) {
		        byte[] result = {};
		        try {
		            result =  service.getAlarmDetail(res);
		            return result;
		        }catch (Exception e) {
		            e.printStackTrace();
		        }
		        return result;
		    }

	@PostMapping("deletes")
	@Override
	public ResultDTO<ProjectElectricPower>  deletes(@RequestBody  RequestDTO<ProjectElectricPower> t){
		hasPermission(deleteRole());
		try {
			List<ProjectDeviceStock> list= new ArrayList<>();
			for(int i=0;i<t.getIds().size();i++){
				Integer id= (Integer) t.getIds().get(i);
				EntityWrapper<ProjectElectricPower> wrapper =new EntityWrapper<>();
				wrapper.eq("id",id);
				ProjectElectricPower electricPower=  service.selectOne(wrapper);
				if(electricPower !=null){
					Map<String, Object> map = new HashMap<>();
					map.put("device_no",electricPower.getDeviceNo());
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

}
