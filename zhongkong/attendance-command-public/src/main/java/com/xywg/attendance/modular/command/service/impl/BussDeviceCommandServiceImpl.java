package com.xywg.attendance.modular.command.service.impl;

import com.xywg.attendance.modular.command.model.BussDeviceCommand;
import com.xywg.attendance.modular.command.dao.BussDeviceCommandMapper;
import com.xywg.attendance.modular.command.service.IBussDeviceCommandService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
@Service
public class BussDeviceCommandServiceImpl extends ServiceImpl<BussDeviceCommandMapper, BussDeviceCommand> implements IBussDeviceCommandService {


    @Override
    public void insertBatchSqlServer(List<BussDeviceCommand> commandToDbList) {
        baseMapper.insertBatchSqlServer(commandToDbList);
    }
}
