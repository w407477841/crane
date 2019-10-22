package com.xywg.equipmentmonitor.modular.baseinfo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.modular.baseinfo.model.MasterDeviceType;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2018-08-18
 */
public interface IMasterDeviceTypeService extends IService<MasterDeviceType> {
    /**
     * 获取设备类型列表
     * @return
     */
    List<MasterDeviceType> selectDeviceType();

    /**
     * 获取设备类型
     * @param page
     * @param requestDTO
     * @return
     * @throws Exception
     */
    List<MasterDeviceType> selectDeviceTypeInfo(Page<MasterDeviceType> page, RequestDTO<MasterDeviceType> requestDTO) throws Exception;
}
