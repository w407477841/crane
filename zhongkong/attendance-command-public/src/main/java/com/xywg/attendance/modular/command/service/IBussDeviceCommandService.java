package com.xywg.attendance.modular.command.service;

import com.xywg.attendance.modular.command.model.BussDeviceCommand;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
public interface IBussDeviceCommandService extends IService<BussDeviceCommand> {

    /**
     * 批量插入
     * @param commandToDbList
     */
    void insertBatchSqlServer(List<BussDeviceCommand> commandToDbList);

}
