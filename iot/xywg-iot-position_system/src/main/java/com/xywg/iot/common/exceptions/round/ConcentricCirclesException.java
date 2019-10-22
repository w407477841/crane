package com.xywg.iot.common.exceptions.round;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:24 2019/3/5
 * Modified By : wangyifei
 */
public class ConcentricCirclesException extends Exception {

    public ConcentricCirclesException() {
    }

    public ConcentricCirclesException(String message) {
        super(message);
    }

    public ConcentricCirclesException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcentricCirclesException(Throwable cause) {
        super(cause);
    }

    public ConcentricCirclesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
