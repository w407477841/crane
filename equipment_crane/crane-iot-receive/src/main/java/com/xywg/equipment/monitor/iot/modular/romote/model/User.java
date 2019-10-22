package com.xywg.equipment.monitor.iot.modular.romote.model;

import lombok.Data;

import java.util.Date;


/**
 * @author hjy
 */
@Data
public class User {

   /* private static final long serialVersionUID = 1L;*/

    /**
     * ID
     */
    private Integer id;
    /**
     * 所属部门
     */
    private Integer orgId;
    /**
     * 用户编码
     */
    private String code;
    /**
     * 用户手机
     */
    private String phone;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 来源标记
     */
    private Integer flag;
    /**
     * 关联用户id
     */
    private Integer relationUserId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String comments;

    private Integer isDel;
    private Date createTime;
    private String createUser;
    private Date modifyTime;
    private String modifyUser;
}
