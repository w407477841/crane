package com.xywg.attendance.modular.command.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author hjy
 * @date 2019/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("Base_Project")
public class BaseProject extends Model<BaseProject> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增长
     */
    @TableId(value="ID", type= IdType.AUTO)
    private String id;



    @TableField("XMBM")
    private String xmbm;


    @TableField("XMMC")
    private String xmmc;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
