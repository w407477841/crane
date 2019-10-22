package com.xywg.equipmentmonitor.modular.device.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDeviceStock;
import com.xywg.equipmentmonitor.modular.station.service.impl.ProjectDeviceStockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectLiftDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLift;
import com.xywg.equipmentmonitor.modular.device.service.IProjectLiftService;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectLiftVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
*Description:
*Company:星云网格
*@author yy
*@date 2018年8月21日 
*/
@Api(tags= {"升降机"})
@RestController
@RequestMapping("/ssdevice/device/projectLift")
public class ProjectLiftController extends BaseController<ProjectLift,IProjectLiftService> {

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
	 * 新增
	 * @param res
	 * @return
	 */
	@PostMapping("insertProjectLift")
	@ApiOperation("新增")
	ResultDTO<Object> insertProjectLift(@RequestBody RequestDTO<ProjectLiftDTO> res){
	    try {
	    	if(service.insertProjectLift(res)) {
				return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
			}
		} catch (Exception e) {
	    	e.printStackTrace();
	    	if("设备名称重复".equals(e.getMessage())) {
	    		return ResultDTO.fail("工程名称和设备编号不能同时重复");
			}
		}
		return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
	}

	/**
	 * 编辑
	 * @param res
	 * @return
	 */
	@PostMapping("updateProjectLift")
	@ApiOperation("修改")
	ResultDTO<Object> updateProjectLift(@RequestBody RequestDTO<ProjectLiftDTO> res){
		try {
			if(service.updateProjectLift(res)) {
				return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if("设备名称重复".equals(e.getMessage())) {
				return ResultDTO.fail("工程名称和设备编号不能同时重复");
			}
		}
		return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
	}
	
	/**
	 * 升降机地图用
	 *
	 * @param res
	 * @return
	 */
	@ApiOperation("升降机地图用")
	@PostMapping("selectListMap")
	ResultDTO<List<ProjectLiftVO>> selectListMap(@RequestBody RequestDTO<ProjectLiftVO> res) {
		return service.selectListMap(res);
	}
	
	/**
	 * 分页查询
	 * @param res
	 * @return
     * @author yuanyang
	 */
	@ApiOperation("一览")
	@PostMapping("selectPageList")
	ResultDTO<DataDTO<List<ProjectLiftVO>>> selectPageList(@RequestBody RequestDTO<ProjectLiftVO> res){
		return service.selectPageList(res);
	}

    /**
     * 单条
     *
     * @param res
     * @return
     * @author yuanyang
     */
    @ApiOperation("单条详情")
    @PostMapping("selectInfo")
    public ResultDTO<ProjectLiftDTO> selectInfo(@RequestBody RequestDTO<ProjectLiftDTO> res) {
        return service.selectInfo(res);
    }

	/**
	 * 启用
	 * @param res
	 * @return
	 */
	@ApiOperation("启用")
	@PostMapping("updateStatus")
	public ResultDTO<Object> updateStatus(@RequestBody RequestDTO<ProjectLiftDTO> res) {
			return service.updateStatus(res);
	}

	@PostMapping("deletes")
	@Override
	public ResultDTO<ProjectLift>  deletes(@RequestBody  RequestDTO<ProjectLift> t){
		hasPermission(deleteRole());
		try {
			List<ProjectDeviceStock> list = new ArrayList<>();
			for(int i=0;i<t.getIds().size();i++){
				Integer id= (Integer) t.getIds().get(i);
				EntityWrapper<ProjectLift> wrapper =new EntityWrapper<>();
				wrapper.eq("id",id);
				ProjectLift lift=  service.selectOne(wrapper);
				if(lift !=null){
					Map<String, Object> map = new HashMap<>();
					map.put("device_no",lift.getDeviceNo());
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
	public ResultDTO<Object> updateDispatch(@RequestBody RequestDTO<ProjectLift> res) {
		try {
			return service.updateDispatch(res);
		} catch (Exception e) {
			return new ResultDTO<>(false);
		}
	}
}
