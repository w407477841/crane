package com.xingyun.equipment.crane.modular.alipay.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.Const;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.alipay.model.ProjectTopUp;
import com.xingyun.equipment.crane.modular.alipay.dao.ProjectTopUpMapper;
import com.xingyun.equipment.crane.modular.alipay.service.IProjectTopUpService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouyujie
 * @since 2019-06-25
 */
@Service
public class ProjectTopUpServiceImpl extends ServiceImpl<ProjectTopUpMapper, ProjectTopUp> implements IProjectTopUpService {

    @Override
    public ResultDTO<DataDTO<List<ProjectTopUp>>> getPageList(RequestDTO request) {
        Page<ProjectTopUp> page = new Page<ProjectTopUp>(request.getPageNum(), request.getPageSize());
        EntityWrapper<RequestDTO> ew = new EntityWrapper<RequestDTO>();
        if(request.getFlag()){
            ew.eq("a.is_del", 0);
        }else{
            ew.eq("a.is_del", 0).in("b.org_id",Const.orgIds.get());


        }
        if (null != request.getKey() && !request.getKey().isEmpty()) {
            ew.andNew().like("a.crane_no", request.getKey()).or().like("a.gprs",request.getKey()).or().like("a.device_no",request.getKey());
        }
        if(null != request.getStatus()){
            ew.eq("a.status", request.getStatus());
        }

        List<ProjectTopUp> list = baseMapper.selectPageList(page, ew);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }
}
