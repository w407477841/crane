package com.xingyun.equipment.plugins.craneplugin.service;


import com.xingyun.equipment.plugins.core.callback.CommandCallback;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:47 2019/7/9
 * Modified By : wangyifei
 */
public abstract class BaseCommandService implements CommandService{

    protected final CommandCallback callback;

    public BaseCommandService(CommandCallback callback) {
        this.callback = callback;
    }

}
