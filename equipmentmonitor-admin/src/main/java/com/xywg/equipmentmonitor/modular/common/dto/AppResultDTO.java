package com.xywg.equipmentmonitor.modular.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xywg.equipmentmonitor.core.common.constant.AppCodeEnum;
import com.xywg.equipmentmonitor.core.common.constant.ResultCodeEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResultDTO<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * message
     */
    private String message;
    /**
     * 数据
     */
    private T data;

    public AppResultDTO() {
    }


    /**
     * 构造函数
     *
     * @param code
     * @param data
     * @param message
     */
    public AppResultDTO(Integer code, T data) {
        this(code, data, null);
    }

    /**
     * 构造函数
     *
     * @param code
     * @param data
     * @param message
     */
    public AppResultDTO( T data, String message) {
        this.code = 200;
        this.data = data;
        this.message = message == null ? code.equals(200) ? ResultCodeEnum.SUCCESS.msg() : ResultCodeEnum.FAIL.msg() : message;
    }

    /**
     * 构造函数
     *
     * @param code
     * @param data
     * @param message
     */
    public AppResultDTO(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message == null ? code.equals(200) ? ResultCodeEnum.SUCCESS.msg() : ResultCodeEnum.FAIL.msg() : message;
    }

    /**
     * 工厂
     * @param t
     * @param <T>
     * @return
     */
    public static <T> AppResultDTO<T> factory(T t) {
        AppResultDTO<T> dto = new AppResultDTO(200, t);
        return dto;
    }

    /**
     * 成功
     *
     * @param message
     * @return
     */
    public static <T> AppResultDTO<T> response(AppCodeEnum codeEnum) {
        AppResultDTO<T> dto = new AppResultDTO();
        dto.setCode(codeEnum.code());
        dto.setMessage(codeEnum.msg());
        return dto;
    }

    /**
     * 失败
     *
     * @param message
     * @return
     */
    public static <T> AppResultDTO<T> fail(String message) {
        AppResultDTO<T> dto = new AppResultDTO();
        dto.setCode(300);
        dto.setMessage(message);
        return dto;
    }
    
    /**
     * 成功
     *
     * @param message
     * @return
     */
    public static <T> AppResultDTO<T> success(String message) {
        AppResultDTO<T> dto = new AppResultDTO();
        dto.setCode(200);
        dto.setMessage(message);
        return dto;
    }
}
