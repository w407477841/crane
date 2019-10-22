package com.xingyun.equipment.crane.modular.alipay.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouyujie
 * @since 2019-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectTopUpDTO extends Model<ProjectTopUpDTO> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 充值时间
     */
    @TableField("charge_time")
    private Integer chargeTime;
    /**
     * 充值金额
     */
    @TableField("charge_money")
    private BigDecimal chargeMoney;
    /**
     * 删除标志
     */
    @TableField(value = "is_del",fill = FieldFill.INSERT)
    private Integer isDel;
    /**
     * 创建日期
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 创建人
     */
    @TableField(value = "create_user",fill = FieldFill.INSERT)
    private Integer createUser;
    /**
     * 修改日期
     */
    @TableField("modify_time")
    private Date modifyTime;
    /**
     * 修改人
     */
    @TableField("modify_user")
    private Integer modifyUser;

    /**
     * 单价
     */
    private double price;

    private List<ProjectDeviceDTO> devices;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ProjectTopUp{" +
        ", id=" + id +

        ", chargeTime=" + chargeTime +
        ", chargeMoney=" + chargeMoney +
        ", isDel=" + isDel +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        ", modifyTime=" + modifyTime +
        ", modifyUser=" + modifyUser +
        "}";
    }
}
