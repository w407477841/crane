package com.xingyun.equipment.crane.modular.infromation.controller;


import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xingyun.equipment.core.enums.OperationEnum;
import com.xingyun.equipment.core.BaseController;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.infromation.model.ProjectAnnouncement;
import com.xingyun.equipment.crane.modular.infromation.service.ProjectAnnouncementService;
import com.xingyun.equipment.crane.modular.infromation.vo.ProjectAnnouncementVO;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-21
 */
@RestController
@RequestMapping("/admin-crane/infromation/projectAnnouncement")
public class ProjectAnnouncementController extends BaseController<ProjectAnnouncement,ProjectAnnouncementService> {

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
	  * @param t
	  * @return
	  */
	 @PostMapping("insertProjectAnnouncement")
	 public ResultDTO<ProjectAnnouncementVO>  insertProjectAnnouncement(@RequestBody RequestDTO<ProjectAnnouncementVO> t){
		 try {
			if( service.insertProjectAnnouncement(t.getBody())){
				 return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
	 }
	 /**
	  * 编辑
	  * @param t
	  * @return
	  */
	 @PostMapping("updateProjectAnnouncement")
	 public ResultDTO<ProjectAnnouncementVO>  updateProjectAnnouncement(@RequestBody RequestDTO<ProjectAnnouncementVO> t){
		 try {
			if( service.updateProjectAnnouncement(t.getBody())){
				 return ResultDTO.resultFactory(OperationEnum.UPDATE_SUCCESS);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return ResultDTO.resultFactory(OperationEnum.UPDATE_ERROR);
	 }
	 /**
	  * 删除
	  * @param t
	  * @return
	  */
	 @PostMapping("deleteProjectAnnouncement")
	 public ResultDTO<ProjectAnnouncement>  deleteProjectAnnouncement(@RequestBody  RequestDTO<ProjectAnnouncement> t){
		 try {
			if( service.deleteProjectAnnouncement(t.getIds())){
				 return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return ResultDTO.resultFactory(OperationEnum.DELETE_ERROR);
	 }
	 
	 @ApiOperation("条件分页查询")
	    @PostMapping("/selectPageList")
	    public ResultDTO<DataDTO<List<ProjectAnnouncement>>> selectPageList(@RequestBody RequestDTO<ProjectAnnouncement> request){
	        return service.getPageList(request);
	    }
	 @ApiOperation("查询单条")
	    @PostMapping("/selectProjectAnnouncementOne")
	    public ResultDTO<ProjectAnnouncementVO> selectProjectAnnouncementOne(@RequestBody RequestDTO<ProjectAnnouncement> request){
		 ProjectAnnouncementVO result = service.selectProjectAnnouncementOne(request.getId());
	        return new ResultDTO<ProjectAnnouncementVO>(true,result,null);
	    }

}

