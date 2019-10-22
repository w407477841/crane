package com.xywg.attendance.modular.handler.controller;

import com.xywg.attendance.common.global.GlobalStaticConstant;
import com.xywg.attendance.common.model.ResultDTO;
import com.xywg.attendance.modular.handler.model.Command;
import com.xywg.attendance.modular.handler.model.CommandTypeEnum;
import com.xywg.attendance.modular.handler.service.CommandService;
import com.xywg.attendance.modular.handler.thread.BaseThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hjy
 * @date 2019/4/16
 * 命令下发处理
 */
@RestController
@RequestMapping("/deviceCommand")
public class CommandController {
    @Autowired
    private CommandService commandService;


    /**
     * 下发指令
     */
    @RequestMapping("/issued")
    public ResultDTO issued(@RequestBody Command command) {

        if (command.getDeviceSnList().size() == 0 || command.getCommandType() == null) {
            return ResultDTO.resultError("设备编号和命令类型必需存在", null);
        }
        CommandTypeEnum typeEnum = CommandTypeEnum.getMethodName(command.getCommandType());
        if (typeEnum == null) {
            return ResultDTO.resultError("命令类型不存在", null);
        }
        // 丢进线程池
        GlobalStaticConstant.threadPoolExecutor.execute(new BaseThread(commandService,command));

        return ResultDTO.resultSuccess("ok",null);
    }



}
