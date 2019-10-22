package com.xywg.equipment.monitor.modular.whf.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author xss
 * @since 2018-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_project_crane")
public class ProjectCrane extends Model<ProjectCrane> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 工程名称
     */
    @TableField("project_id")
    private Integer projectId;
    /**
     * 产权编号
     */
    private String identifier;
    /**
     * 规格型号
     */
    private String specification;
    /**
     * 产权单位
     */
    private String owner;
    /**
     * 制造厂家
     */
    private String manufactor;
    /**
     * 最大幅度
     */
    @TableField("max_range")
    private BigDecimal maxRange;
    /**
     * 最大载重量
     */
    @TableField("max_weight")
    private BigDecimal maxWeight;
    /**
     * 标准高度
     */
    @TableField("standard_height")
    private BigDecimal standardHeight;
    /**
     * 额定力矩
     */
    @TableField("fix_moment")
    private BigDecimal fixMoment;
    /**
     * 风速
     */
    @TableField("wind_speed")
    private BigDecimal windSpeed;
    /**
     * 倾角
     */
    @TableField("tilt_angle")
    private BigDecimal tiltAngle;
    /**
     * 在线状态
     */
    @TableField("is_online")
    private Integer isOnline;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 位置
     */
    @TableField("place_point")
    private String placePoint;
    /**
     * 备注
     */
    private String comments;
    /**
     * 组织结构
     */
    @TableField("org_id")
    private Integer orgId;
    /**
     * 设备编号
     */
    @TableField("device_no")
    private String deviceNo;
    /**
     * GPRS
     */
    private Integer gprs;

    private String name;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    /**
     * 删除标志
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDel;
    /**
     * 创建日期
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 创建人
     */
    @TableField(value="create_user",fill = FieldFill.INSERT)
    private Integer createUser;
    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private String createUserName;
    /**
     * 修改日期
     */
    @TableField(value = "modify_time", fill = FieldFill.UPDATE)
    private Date modifyTime;
    /**
     * 修改人
     */
    @TableField(value="modify_user", fill = FieldFill.UPDATE)
    private Integer modifyUser;


}
