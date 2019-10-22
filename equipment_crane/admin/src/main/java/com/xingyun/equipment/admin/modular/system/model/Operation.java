package com.xingyun.equipment.admin.modular.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xingyun.equipment.admin.core.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf123
 * @since 2018-08-13
 */
@Data
@TableName("t_sys_operation")
public class Operation extends BaseEntity<Operation> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 上级ID
     */
    @TableField("parent_id")
    private Integer parentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 等级
     */
    private Integer level;
    /**
     * url
     */
    private String url;
    /**
     * 权限字符
     */
    private String permission;
    /**
     * 类型
     */
    private String type;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String comments;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
