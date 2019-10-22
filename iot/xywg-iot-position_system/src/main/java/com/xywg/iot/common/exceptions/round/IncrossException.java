package com.xywg.iot.common.exceptions.round;

/**
 * @author : wangyifei
 * Description 内交
 * Date: Created in 9:25 2019/3/5
 * Modified By : wangyifei
 */
public class IncrossException extends Exception {

    public IncrossException() {
    }

    public IncrossException(String message) {
        super(message);
    }

    public IncrossException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncrossException(Throwable cause) {
        super(cause);
    }

    public IncrossException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
