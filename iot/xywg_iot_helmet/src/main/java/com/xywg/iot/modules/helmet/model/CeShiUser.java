package com.xywg.iot.modules.helmet.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlType;

/**
 * @author hjy
 * @date 2018/12/11
 */
@Data
@XmlType(propOrder = { "userName", "userAge","userAddress" })
public class CeShiUser {
    String userName;
    String userAge;
    String userAddress;

    public CeShiUser() {
    }

    public CeShiUser(String userName, String userAge, String userAddress) {
        this.userName = userName;
        this.userAge = userAge;
        this.userAddress = userAddress;
    }
}
