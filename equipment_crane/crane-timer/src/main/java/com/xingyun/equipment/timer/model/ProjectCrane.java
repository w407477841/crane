package com.xingyun.equipment.timer.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @TableField(exist = false)
    private String projectName;
    @TableField(exist = false)
    private Integer builder;

    /**
     * 黑匣子编号
     */
    @TableField("device_no")
    private String deviceNo;
    /**
     * 黑匣子型号
     */
    @TableField("model")
    private String model;

    /**
     * 黑匣子生产厂商
     */
    @TableField("device_manufactor")
    private String deviceManufactor;
    /**
     * 黑匣子安装日期
     */
    @TableField("assemble_date")
    private Date assembleDate;

    @TableField("alarm_status")
    private Integer alarmStatus;
    /**
     * GPRS
     */
    @TableField("gprs")
    private String gprs;

    /**
     * currentHight
     */
    @TableField("current_height")
    private String currentHeight;


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
     * 制造许可证
     */
    @TableField("licence")
    private String licence;

    /**
     * 安装单位
     */
    @TableField("assemble_unit")
    private String assembleUnit;
    /**
     * 设备出厂日期
     */
    @TableField("production_date")
    private Date productionDate;

    /**
     * 检测日期
     */
    @TableField("test_date")
    private Date testDate;

    /**
     * 安装日期
     */
    @TableField("crane_assemble_date")
    private Date craneAssembleDate;

    /**
     * 预计拆除日期
     */
    @TableField("disassemble_date")
    private Date disassembleDate;

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
     * 附着道数
     */
    private Integer turn;

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
    @TableField(value = "org_id", fill = FieldFill.INSERT)
    private Integer orgId;
    /**
     * 固件版本
     */
    @TableField("firmware_version")
    private String firmwareVersion;
    /**
     * 固件升级时间
     */
    @TableField("upgrade_time")
    private Date upgradeTime;
    /**
     * 塔机编号
     */
    @TableField("crane_no")
    private String craneNo;
    /**
     * 名称
     */
    private String name;

    /**
     * 倍率
     */
    @TableField("multiple_rate")
    private Integer multipleRate;
    /**
     * 功能配置
     */
    @TableField("function_config")
    private String functionConfig;
    /**
     * 识别配置
     */
    @TableField("recognition_config")
    private String recognitionConfig;

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
    @TableField(value = "create_user", fill = FieldFill.INSERT)
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
    @TableField(value = "modify_user", fill = FieldFill.UPDATE)
    private Integer modifyUser;




}
