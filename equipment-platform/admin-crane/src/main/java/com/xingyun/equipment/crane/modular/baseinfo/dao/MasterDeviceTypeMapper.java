package com.xingyun.equipment.crane.modular.baseinfo.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.crane.modular.baseinfo.model.MasterDeviceType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyf
 * @since 2018-08-18
 */
@Mapper
public interface MasterDeviceTypeMapper extends BaseMapper<MasterDeviceType> {
    /**
     * 根据名称获取设备类型
     * @param map
     * @return
     * @throws Exception
     */
    List<MasterDeviceType> getDeviceTypeByName(Map<String, Object> map);

    /**
     * 获取设备类型列表
     * @return
     */
    List<MasterDeviceType> selectDeviceType();

    /**
     * 获取设备类型
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<MasterDeviceType> selectDeviceTypeInfo(Page<MasterDeviceType> page, Map<String, Object> map) throws Exception;
}
