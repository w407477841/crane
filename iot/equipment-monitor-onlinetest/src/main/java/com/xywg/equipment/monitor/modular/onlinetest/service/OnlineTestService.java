package com.xywg.equipment.monitor.modular.onlinetest.service;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:05 2019/1/21
 * Modified By : wangyifei
 */
public interface OnlineTestService {
    /**
     *
     * @param v  厂家
     * @param type 类型
     * @param sn 编号
     * @param data 数据
     */
    void test(String v,String type,String sn,String data);

}
