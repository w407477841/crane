package com.xywg.iot.common.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hjy
 * @date 2018/11/20
 */
@Data
public class BaseEntity<T extends BaseEntity> extends Model<T>{

    @TableId(value = "id", type = IdType.AUTO)
    private  Integer id;
    /**
     * 删除标志
     */
    @TableField(value = "is_del")
    @TableLogic
    private Integer isDel;
    /**
     * 创建日期
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private Integer createUser;
    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private String createUserName;
    /**
     * 修改日期
     */
    @TableField(value = "modify_time")
    private Date modifyTime;
    /**
     * 修改人
     */
    @TableField(value = "modify_user")
    private Integer modifyUser;

    /**
     * 表名称
     */
    @TableField(exist = false)
    private String tableName;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
