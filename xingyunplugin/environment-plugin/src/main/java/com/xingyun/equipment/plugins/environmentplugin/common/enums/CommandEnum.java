package com.xingyun.equipment.plugins.environmentplugin.common.enums;

import cn.hutool.core.util.ReflectUtil;
import com.xingyun.equipment.plugins.core.callback.CommandCallback;
import com.xingyun.equipment.plugins.core.dto.ProtocolDTO;
import com.xingyun.equipment.plugins.environmentplugin.service.CommandService;
import com.xingyun.equipment.plugins.environmentplugin.service.impl.*;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:08 2019/7/8
 * Modified By : wangyifei
 */
public enum CommandEnum {
    /**心跳*/
    HEARTBEAT(2,HeatbeatCommandServiceImpl.class),
    /**同步时间*/
    LOCK_IN(3,LockInCommandServiceImpl.class),
    /**监控数据*/
    ENVIROMENT(4,EnviromentCommandServiceImpl.class),
    /** 异常数据 */
    EXCEPTION(8,ExceptionCommandServiceImpl.class),
    /** 重启 */
    REBOOT(9,RebootCommandServiceImpl.class),
    /** 基本信息 */
    BASIC_INFO(10,BasicInfoCommandServiceImpl.class),

    ;

    private Integer command;
    private Class<? extends CommandService> commandService;

    CommandEnum(Integer command, Class<? extends CommandService> commandService) {
        this.command = command;
        this.commandService = commandService;
    }

    public Integer getCommand() {
        return command;
    }

    public Class<? extends CommandService> getCommandService() {
        return commandService;
    }

    /**
     * 执行 对应的指令
     * @param protocolDTO 数据
     */
    public static void doCommand(ProtocolDTO protocolDTO, ChannelHandlerContext ctx, CommandCallback commandCallback){
        for(CommandEnum commandEnum:CommandEnum.values()){
            if(commandEnum.getCommand().equals(protocolDTO.getCommand())){
                    CommandService commandService =  ReflectUtil.newInstance(commandEnum.getCommandService(),commandCallback);
                    commandService.commandExec(protocolDTO,ctx);
                return ;
            }
        }

        System.out.println("未匹配掉对应的指令 "+protocolDTO.getCommand());

    }

}
