package com.xingyun.equipment.crane.modular.alipay.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.modular.alipay.model.ProjectTopUp;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyujie
 * @since 2019-06-25
 */
public interface IProjectTopUpService extends IService<ProjectTopUp> {

    public ResultDTO<DataDTO<List<ProjectTopUp>>> getPageList(RequestDTO request);
}
