package com.xywg.attendance.common.model;

import lombok.Data;

/**
 * @author hjy
 */
@Data
public class ResultDTO {

    private String msg;

    private Integer code;

    private Object data;


    public ResultDTO(String msg, Integer code, Object data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }


    public ResultDTO() {
    }


    public static ResultDTO resultSuccess(String msg, Object data) {
        return new ResultDTO(msg, 200, data);
    }

    public static ResultDTO resultError(String msg, Object data) {
        return new ResultDTO(msg, 400, data);
    }

    public static ResultDTO resultMessage(Integer code, String msg, Object data) {
        return new ResultDTO(msg, code, data);
    }
}
