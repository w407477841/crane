package com.xywg.iot.netty.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:36 2019/2/28
 * Modified By : wangyifei
 */
public abstract class BaseCalcThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCalcThread.class);

    private final String key ;

    public BaseCalcThread(String key) {
        this.key = key;
    }

    @Override
    public void run() {
        run(this.key);
    }
    protected  abstract void run(String key);


}
