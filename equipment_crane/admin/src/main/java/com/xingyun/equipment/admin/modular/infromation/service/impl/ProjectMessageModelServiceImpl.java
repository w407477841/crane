package com.xingyun.equipment.admin.modular.infromation.service.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.infromation.dao.ProjectMessageModelMapper;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectMessageModel;
import com.xingyun.equipment.admin.modular.infromation.service.ProjectMessageModelService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouyujie
 * @since 2018-08-21
 */
@Service
public class ProjectMessageModelServiceImpl extends ServiceImpl<ProjectMessageModelMapper, ProjectMessageModel> implements ProjectMessageModelService {

	/**
	 * 新增短信
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public ResultDTO<Object> insertMessage(ProjectMessageModel p) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Integer value = Integer.parseInt(sdf.format(calendar.getTime()));
		System.out.println(value);
		String currentCode = baseMapper.getDocumentCode( value);
        System.out.println(currentCode);
		if (currentCode == null || "".equals(currentCode) || "null".equals(currentCode)) {
			currentCode = "0001";
		} else {
			currentCode = String.valueOf(Integer.valueOf(currentCode) + 1);
		}
		String num = "";
		for (int i = 0; i < 4 - (currentCode.length()); i++) {
			num += "0";
		}
		String code = value+num+currentCode;
		
		baseMapper.plusCallTimes(p.getDeviceType());
		System.out.println(code);
		p.setCode(code);
		p.setStatus(0);
		System.out.println(p);
		baseMapper.insert(p);
		return new ResultDTO<>(true,null,"新增成功");
	}
	
	/**
	 * 修改
	 * @param entity
	 * @return
	 */
	 @Transactional(rollbackFor = Exception.class)
	    @Override
	    public boolean updateById(ProjectMessageModel entity) {
		 ProjectMessageModel p =  baseMapper.selectById(entity.getId());
		 baseMapper.minusCallTimes(p.getDeviceType());
		 baseMapper.plusCallTimes(entity.getDeviceType());
	        return retBool(baseMapper.updateById(entity));
	    }
	 
	 /**
	  * 批量删除
	  * @param idList
	  * @return
	  */
	 @Transactional(rollbackFor = Exception.class)
	    @Override
	    public boolean deleteBatchIds(List<? extends Serializable> idList) {
		 for(int i=0;i<idList.size();i++) {
			 Object id = idList.get(i);
			 ProjectMessageModel p = baseMapper.selectById((Serializable) id);
			 baseMapper.minusCallTimes(p.getDeviceType());
		 }
	        return retBool(baseMapper.deleteBatchIds(idList));
	    }

	/**
	 * 分页查询
	 */
	@Override
	public ResultDTO<DataDTO<List<ProjectMessageModel>>> selectPageList(RequestDTO<ProjectMessageModel> res) {
		Page<ProjectMessageModel> page = new Page<ProjectMessageModel>(res.getPageNum(),res.getPageSize());
		EntityWrapper<RequestDTO<ProjectMessageModel>> ew = new EntityWrapper<RequestDTO<ProjectMessageModel>>();
		ew.eq("a.is_del", 0);
		
		List<ProjectMessageModel> list = baseMapper.selectPageList(page, ew);
		return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
	}

	/**
	 * 启用
	 * @param res
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public ResultDTO<Object> updateStatus(RequestDTO<ProjectMessageModel> res) {
		boolean flag =true;
		
		for(int i=0;i<res.getIds().size();i++) {
			Integer id = Integer.valueOf(res.getIds().get(i).toString());
			ProjectMessageModel message=baseMapper.selectById(id);
			EntityWrapper<ProjectMessageModel> ew = new EntityWrapper<ProjectMessageModel>();
			ew.eq("is_del", 0);
			ew.eq("status", 1);
			ew.eq("type", message.getType());
			ew.eq("device_type", message.getDeviceType());
			Integer count=baseMapper.selectCount(ew);
			if(count >0) {
				flag =false;
				continue;
			}
			ProjectMessageModel message1 = new ProjectMessageModel();
			message1.setStatus(1);
		    message1.setId(id);
		    baseMapper.updateById(message1);
		}
		if(!flag) {
			return new ResultDTO<>(false,null,"同一设备,同一短信类型的模板只有一条能在启用状态");
		}else {
			return new ResultDTO<>(true,null,"启用成功");
		}
	}
	/**
	 * 单条
	 * @param id
	 * @return
	 */
	@Override
	public ResultDTO<ProjectMessageModel> getOne(Integer id) {
		ProjectMessageModel message =baseMapper.getOne(id);
		return new ResultDTO<>(true,message);
	}

}
