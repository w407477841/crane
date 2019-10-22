package com.xywg.iot.common.exceptions.round;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:27 2019/3/5
 * Modified By : wangyifei
 */
public class IncludeException extends  Exception {

    public IncludeException() {
    }

    public IncludeException(String message) {
        super(message);
    }

    public IncludeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncludeException(Throwable cause) {
        super(cause);
    }

    public IncludeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
