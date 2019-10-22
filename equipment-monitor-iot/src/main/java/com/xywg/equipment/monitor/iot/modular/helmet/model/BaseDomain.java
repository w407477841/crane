package com.xywg.equipment.monitor.iot.modular.helmet.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hjy
 * @date 2018/11/22
 */
public class BaseDomain<T extends BaseDomain> extends Model<T> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "comments")
    private String comments;


    @TableField(value = "is_del")
    private Integer isDel;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "create_user")
    private Integer createuser;

    @TableField(exist = false)
    private String createuserName;

    @TableField(value = "modify_time")
    private Date modifyTime;

    @TableField(value = "modify_user")
    private Integer modifyUser;

    @TableField(exist = false)
    private String modifyUserName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
