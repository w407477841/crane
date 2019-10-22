package com.xywg.attendance.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hjy
 * @date 2019/3/4
 * 传输数据模板公用类
 */
@Data
public class TransmissionMessageTemplate implements Serializable {
    private static final long serialVersionUID = -8691064247933856860L;
    /**
     * 客户端ip地址
     */
    private String addressIp;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 请求路径
     */
    private String url;

    /**
     * body 所有带参数
     */
    private String body;

    /**
     * 收到的时间
     */
    private String date;

    public TransmissionMessageTemplate(String addressIp, String method, String url, String body, String date) {
        this.addressIp = addressIp;
        this.method = method;
        this.url = url;
        this.body = body;
        this.date = date;
    }

    public TransmissionMessageTemplate() {
    }
}
