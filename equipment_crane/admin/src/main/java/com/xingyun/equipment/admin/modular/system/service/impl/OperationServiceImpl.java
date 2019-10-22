package com.xingyun.equipment.admin.modular.system.service.impl;


import com.xingyun.equipment.admin.core.common.constant.Const;

import com.xingyun.equipment.admin.core.dto.RequestDTO;

import com.xingyun.equipment.admin.modular.system.model.Operation;
import com.xingyun.equipment.admin.modular.system.dao.OperationMapper;
import com.xingyun.equipment.admin.modular.system.service.IOperationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xingyun.equipment.admin.modular.system.vo.OperationVo;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-16
 */
@Service
public class OperationServiceImpl extends ServiceImpl<OperationMapper, Operation> implements IOperationService {
    @Override
    public List<OperationVo> selectTreeOperation(RequestDTO<OperationVo> requestDTO) throws Exception {
        Map<String, Object> map = new HashMap<>(10);
        Map<String, Object> map1 = new HashMap<>(10);
        map.put("parentId", 0);
        map1.put("parentId", null);
        ArrayList<Object> list = new ArrayList<>(10);
        List<OperationVo> operationVos = baseMapper.selectOperation(map);
        List<OperationVo> operationVoList = baseMapper.selectOperation(map1);
        if (operationVos.size() > 0) {
            for (int i = 0; i < operationVos.size(); i++) {
                OperationVo operationVo = operationVos.get(i);
                operationVo = getChildren(operationVo, operationVoList);
                list.add(operationVo);
            }
        }
        return operationVos;
    }

    /**
     * 递归添加子集
     *
     * @param operationVo
     * @param operationVos
     * @return
     */
    private OperationVo getChildren(OperationVo operationVo, List<OperationVo> operationVos) {
        List<OperationVo> list = getData(operationVo.getId(), operationVos);
        List<OperationVo> list1 = new ArrayList<>(10);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                OperationVo ov = list.get(i);
                ov = getChildren(ov, operationVos);
                list1.add(ov);
            }
            operationVo.setChildren(list1);
        }
        return operationVo;
    }

    /**
     * 获取子集
     *
     * @param parentId
     * @param operationVos
     * @return
     */
    private List<OperationVo> getData(Integer parentId, List<OperationVo> operationVos) {
        List<OperationVo> operationVoList = new ArrayList<>(10);
        for (int i = 0; i < operationVos.size(); i++) {
            if (parentId.equals(operationVos.get(i).getParentId())) {
                operationVoList.add(operationVos.get(i));
            }
        }
        return operationVoList;

    }

    /**
     * 查询pc端 所有权限
     *
     * @return
     */
    @Override
    public String getPermissions() {
        Integer userId = Const.currUser.get().getId();
        Integer orgId = Const.orgId.get();
        List<Integer> types = Arrays.asList(0);
        List<Integer> levels = Arrays.asList(2,3,4);
        List<Operation> list = baseMapper.getUserOperations(userId, orgId, types,levels);
        StringBuilder sb = new StringBuilder("");

        for (Operation operation : list) {
            sb.append(",").append(operation.getPermission());
        }
        if (sb.length() > 0) {
            return sb.substring(1);
        } else {
            return sb.toString();
        }

    }

    @Override
    public List<Operation> getOperations() {
        Integer userId = Const.currUser.get().getId();
        Integer orgId = Const.orgId.get();
        List<Integer> types = Arrays.asList(0);
        List<Integer> levels = Arrays.asList(2,3,4);
        List<Operation> list = baseMapper.getUserOperations(userId, orgId, types,levels);

        return list;
    }

    @Override
    public List<Operation> selectBatchIds(List<? extends Serializable> idList) {
        return super.selectBatchIds(idList);
    }
}
