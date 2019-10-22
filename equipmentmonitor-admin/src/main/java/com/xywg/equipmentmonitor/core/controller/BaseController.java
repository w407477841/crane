package com.xywg.equipmentmonitor.core.controller;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum;
import com.xywg.equipmentmonitor.core.common.exception.ForbiddenException;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.model.BaseEntity;
import com.xywg.equipmentmonitor.modular.system.model.Role;
import com.xywg.equipmentmonitor.modular.system.service.IRoleService;

import cn.hutool.json.JSONUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
public abstract class BaseController<T extends BaseEntity<T>, S extends IService<T>> {
	@Autowired
 public	 S service;
	
 /**新增权限    返回空为不拦截*/    
public	abstract String insertRole();
/**修改权限    返回空为不拦截*/
public	abstract String updateRole();
/**删除权限    返回空为不拦截*/
public	abstract String deleteRole();

public  abstract String viewRole();
	
	 /**
	  * 新增
	  * @param t
	  * @return
	  */
	 @PostMapping("insert")
	 public ResultDTO<T>  insert(@RequestBody RequestDTO<T> t){
		 
		 hasPermission(insertRole());
		 
		 
		 try {
			if( service.insert(t.getBody())){
				 return ResultDTO.resultFactory(OperationEnum.INSERT_SUCCESS);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return ResultDTO.resultFactory(OperationEnum.INSERT_ERROR);
	 }
	 /**
	  * 修改
	  */
	 @PostMapping("update")
	 public ResultDTO<T>  update(@RequestBody  RequestDTO<T> t){
		 hasPermission(updateRole());
		 try {
			if(service.updateById(t.getBody())){
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
	 @PostMapping("delete")
	 public ResultDTO<T>  delete(@RequestBody  RequestDTO<T> t){
		 hasPermission(deleteRole());
		 try {
			if(service.deleteById(t.getId())){
				 return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return ResultDTO.resultFactory(OperationEnum.DELETE_ERROR);
	 }
	/**
	 * 批量删除
	 * @param t
	 * @return
	 */
	 
	 @PostMapping("deletes")
	 public ResultDTO<T>  deletes(@RequestBody  RequestDTO<T> t){
		 hasPermission(deleteRole());
		 try {
			if(service.deleteBatchIds(t.getIds())){
				 return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return ResultDTO.resultFactory(OperationEnum.DELETE_ERROR);
	 }
	 @PostMapping("get")
	 public ResultDTO<T> get(@RequestBody  RequestDTO<T> t){
		 hasPermission(viewRole());
		 
		 T  result =  service.selectById(t.getId());
		 
		 return new ResultDTO<T>(true,result);
	 }
	 @PostMapping("selectList")
	 public ResultDTO<DataDTO<List<T>>> selectList(@RequestBody  RequestDTO<T> t){
		 hasPermission(viewRole());
		 Page<T> page = new Page<>(t.getPageNum(), t.getPageSize()); 
		 List<T>   ts=   service.selectPage(page).getRecords();
		 return new ResultDTO<DataDTO<List<T>>>(true,DataDTO.factory(ts,page.getTotal()));
	 }
	 @PostMapping("selectListNoPage")
	 public  ResultDTO<List<T>>  selectListNoPage(@RequestBody  RequestDTO<T> t){
		 hasPermission(viewRole());
		 Wrapper  wrapper =new EntityWrapper();
		 wrapper.eq("is_del",0);
		  List<T>  list =  service.selectList(wrapper);
		  return new ResultDTO<>(true,list);

	 }


	 public void hasPermission(String permission){
		 //不设置认为不需要认证
		 if(permission==null||"".equals(permission)){
			 return ;
		 }
		 Collection<? extends GrantedAuthority> auths= SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		 for(GrantedAuthority auth:  auths){
			 if(auth.getAuthority().replace("ROLE_","").equals(permission)){
				 return ;
			 }
		 }
		 throw new ForbiddenException(ResultCodeEnum.NO_PERMISSION.msg());
	 }




}
