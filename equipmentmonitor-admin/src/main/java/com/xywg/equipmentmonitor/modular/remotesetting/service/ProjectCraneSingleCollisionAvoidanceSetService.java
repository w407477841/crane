package com.xywg.equipmentmonitor.modular.remotesetting.service;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.remotesetting.model.ProjectCraneSingleCollisionAvoidanceSet;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xss
 * @since 2018-09-30
 */
@Service
public interface ProjectCraneSingleCollisionAvoidanceSetService extends IService<ProjectCraneSingleCollisionAvoidanceSet>{
    /**
     * 条件分页查询
     * @param request
     * @return
     */
    ResultDTO<DataDTO<List<ProjectCraneSingleCollisionAvoidanceSet>>> getPageList(RequestDTO<ProjectCraneSingleCollisionAvoidanceSet> request);


}
