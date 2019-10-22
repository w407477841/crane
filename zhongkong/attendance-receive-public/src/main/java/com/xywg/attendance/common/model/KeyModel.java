package com.xywg.attendance.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hjy
 * @date 2019/3/19
 * 设备公钥与因子 实体对象
 */
@Data
public class KeyModel  implements Serializable {
    private static final long serialVersionUID = -2303793720773194011L;
    /**
     * 设备编号
     */
    private String  sn;
    /**
     * 初始化加密算法时的内存地址
     */
    private Long initKey;

    private String serverPrivateKey;

    private String devicePublicKey;

    private String serverPublicKey;

    private String deviceFactors;

    private String serverFactors;

    /**
     * 对称加密通信时的秘钥
     */
    private String symmetricalKey;

}
