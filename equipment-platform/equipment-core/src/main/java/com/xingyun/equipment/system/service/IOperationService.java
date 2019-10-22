package com.xingyun.equipment.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.system.model.Operation;
import com.xingyun.equipment.system.vo.OperationVo;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf123
 * @since 2018-08-13
 */
public interface IOperationService extends IService<Operation> {


    /**
     * 查询所有权限
     * @return
     */
    public String getPermissions();

    /**
     * 查询 所有菜单
     * @return
     */
    public List<Operation> getOperations();




    /**
     * 获取权限树形数据
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<OperationVo> selectTreeOperation(RequestDTO<OperationVo> requestDTO) throws Exception;

}
