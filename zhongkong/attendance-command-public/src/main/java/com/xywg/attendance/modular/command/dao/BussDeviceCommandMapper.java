package com.xywg.attendance.modular.command.dao;

import com.xywg.attendance.modular.command.model.BussDeviceCommand;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
public interface BussDeviceCommandMapper extends BaseMapper<BussDeviceCommand> {

    void insertBatchSqlServer(List<BussDeviceCommand> commandToDbList);

}
