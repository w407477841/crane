package com.xywg.equipment.monitor.modular.whf.handler;

import lombok.Data;

import java.util.Objects;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:31 2018/12/11
 * Modified By : wangyifei
 */
@Data
public class XySession {
    /**
     *  每个连接一个id
     */
    private String id;
    /**
     * 设备编号
     */
    private String sn;
    /**
     * 真实号码
     */
    private String number;
    /**
     * 厂家
     */
    private String cj;

    /**
     * 类型
     */
    private String type;
    /**
     *  属性
     */
    private String properties;

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()){ return false;}
        XySession xySession = (XySession) o;
        return Objects.equals(id, xySession.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
