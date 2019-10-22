package com.xingyun.equipment.crane.modular.alipay.model;

import lombok.Data;

@Data
public class OrderDTO {

    /**
     * 设备id
     */
    private String deviceIds;

    /**
     * sim卡
     */
    private String gprs;

    /**
     * 充值时间（相当于数量）
     */
    private Integer chargeTime;

    /**
     * 单价
     */
     private Double price;

    /**
     * 订单总金额   =《商品单价*商品数量》
     */
    private Double totalAmount;

    /**
     * 订单标题
     */
    private String subject;


    /**
     * 买家的支付宝唯一用户号（2088开头的16位纯数字）
     */

    private String buyerId;

}
