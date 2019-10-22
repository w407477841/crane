package com.xywg.equipmentmonitor.modular.baseinfo.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.baseinfo.dao.MasterDeviceTypeMapper;
import com.xywg.equipmentmonitor.modular.baseinfo.model.MasterDeviceType;
import com.xywg.equipmentmonitor.modular.baseinfo.service.IMasterDeviceTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-18
 */
@Service
public class MasterDeviceTypeServiceImpl extends ServiceImpl<MasterDeviceTypeMapper, MasterDeviceType> implements IMasterDeviceTypeService {
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean insert(MasterDeviceType masterDeviceType) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("name",masterDeviceType.getName());
        if(baseMapper.getDeviceTypeByName(map).size() > 0) {
            throw new RuntimeException("名称重复");
        }
        return retBool(this.baseMapper.insert(masterDeviceType));
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean updateById(MasterDeviceType masterDeviceType) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("name",masterDeviceType.getName());
        map.put("id",masterDeviceType.getId());
        if(baseMapper.getDeviceTypeByName(map).size() > 0) {
            throw new RuntimeException("名称重复");
        }
        return retBool(this.baseMapper.updateById(masterDeviceType));
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        for(int i = 0;i < idList.size();i++) {
            MasterDeviceType masterDeviceType = baseMapper.selectById(idList.get(i));
            if(masterDeviceType.getCallTimes() != null) {
                if(masterDeviceType.getCallTimes() > 0) {
                    idList.remove(i);
                }
            }
        }
        boolean isSuccess = false;
        if(idList.size() > 0) {
            isSuccess = super.deleteBatchIds(idList);
        }
        return isSuccess;
    }

    @Override
    public List<MasterDeviceType> selectDeviceType() {
        return baseMapper.selectDeviceType();
    }

    @Override
    public List<MasterDeviceType> selectDeviceTypeInfo(Page<MasterDeviceType> page,RequestDTO<MasterDeviceType> requestDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        return baseMapper.selectDeviceTypeInfo(page,map);
    }
}
