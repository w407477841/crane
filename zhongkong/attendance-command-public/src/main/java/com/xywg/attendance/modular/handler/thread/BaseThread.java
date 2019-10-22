package com.xywg.attendance.modular.handler.thread;

import com.xywg.attendance.modular.handler.model.Command;
import com.xywg.attendance.modular.handler.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:25 2019/4/18
 * Modified By : wangyifei
 */
@Slf4j
public class BaseThread implements Runnable {

    private final CommandService commandService ;
    private final Command command;

    public BaseThread(CommandService commandService,Command command) {
        this.commandService = commandService;
        this.command = command;
    }


    @Override
    public void run() {
        this.commandService.issued(command);
    }
}
