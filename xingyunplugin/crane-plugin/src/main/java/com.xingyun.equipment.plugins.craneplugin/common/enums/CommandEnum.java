package com.xingyun.equipment.plugins.craneplugin.common.enums;

import cn.hutool.core.util.ReflectUtil;
import com.xingyun.equipment.plugins.core.callback.CommandCallback;
import com.xingyun.equipment.plugins.core.dto.ProtocolDTO;
import com.xingyun.equipment.plugins.craneplugin.service.CommandService;
import com.xingyun.equipment.plugins.craneplugin.service.impl.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:08 2019/7/8
 * Modified By : wangyifei
 */
@Slf4j
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
    /** 幅度校准 */
    RANGE(12,RangeCommandServiceImpl.class),
    /** 高度校准 */
    HEIGHT(13,HeightCommandServiceImpl.class),
    /** 角度校准 */
    ANGLE(14,AngleCommandServiceImpl.class),
    /** 重量校准 */
    WEIGHT(15,WeightCommandServiceImpl.class),
    /** 单机防碰撞 */
    SINGLE(16,SingleCommandServiceImpl.class),
    /** 离线监控数据 */
    OFFLINE_ENVIROMENT(17,OfflineEnviromentCommandServiceImpl.class),
    /** 离线工作循环 */
    OFFLINE_CYCLIC(18,OfflineCyclicCommandServiceImpl.class),
    /** 工作循环 */
    CYCLIC(19,CyclicCommandServiceImpl.class),
    /** 工作时长 */
    WORKING_HOURS(20,WorkingHoursCommandServiceImpl.class),
    /** 多机防碰撞 */
    MULTI(21,MultiCommandServiceImpl.class),
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
        log.info("选择指令处理器,params:[{}]",protocolDTO.toString());
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
