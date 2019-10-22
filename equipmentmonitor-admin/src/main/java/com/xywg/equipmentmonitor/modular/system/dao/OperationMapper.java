package com.xywg.equipmentmonitor.modular.system.dao;

import com.xywg.equipmentmonitor.modular.system.model.Operation;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import com.xywg.equipmentmonitor.modular.system.vo.OperationVo;



import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf123
 * @since 2018-08-13
 */
public interface OperationMapper extends BaseMapper<Operation> {


    /**
     *
     * @param userId
     * @param orgId
     * @param types
     * @param levels
     * @return
     */



    List<Operation> getUserOperations(@Param("userId") Integer userId,@Param("orgId") Integer orgId,@Param("types") List<Integer> types,@Param("levels") List<Integer> levels);

    /**
     * 获取权限列表
     * @param map
     * @return
     * @throws Exception
     */
    List<OperationVo> selectOperation(Map<String,Object> map) throws Exception;

}
