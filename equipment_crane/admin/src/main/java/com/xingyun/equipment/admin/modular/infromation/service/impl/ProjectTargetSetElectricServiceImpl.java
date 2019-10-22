package com.xingyun.equipment.admin.modular.infromation.service.impl;

import java.util.List;

import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.infromation.model.ProjectTargetSetElectric;
import com.xingyun.equipment.admin.modular.infromation.dao.ProjectTargetSetElectricMapper;
import com.xingyun.equipment.admin.modular.infromation.service.ProjectTargetSetElectricService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  电表指标设置
 * </p>
 *
 * @author hy
 * @since 2018-09-27
 */
@Service
public class ProjectTargetSetElectricServiceImpl extends ServiceImpl<ProjectTargetSetElectricMapper, ProjectTargetSetElectric> implements ProjectTargetSetElectricService {
	/**
	 * 分页查询
	 */
	@Override
	public ResultDTO<DataDTO<List<ProjectTargetSetElectric>>> getPageList(
			RequestDTO<ProjectTargetSetElectric> request) {
        Page<ProjectTargetSetElectric> page = new Page<ProjectTargetSetElectric>(request.getPageNum(), request.getPageSize());
        EntityWrapper<RequestDTO<ProjectTargetSetElectric>> ew = new EntityWrapper<RequestDTO<ProjectTargetSetElectric>>();
        ew.eq("a.is_del",0);
        if (null != request.getKey() && !request.getKey().isEmpty()) {
            ew.like("a.specification", request.getKey());
        }
        List<ProjectTargetSetElectric> list = baseMapper.selectPageList(page,ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }
	/**
	 * 判重
	 */
	@Override
	public ResultDTO<DataDTO<List<ProjectTargetSetElectric>>> checkBySpecificationAndManufactor(
			RequestDTO<ProjectTargetSetElectric> request) {return new ResultDTO(true, baseMapper.checkBySpecificationAndManufactor(request));}

}
