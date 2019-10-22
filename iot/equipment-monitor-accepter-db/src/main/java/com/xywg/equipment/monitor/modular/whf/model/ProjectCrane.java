package com.xywg.equipment.monitor.modular.whf.model;

import lombok.Data;

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
public class ProjectCrane{

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */

    private Integer id;
    /**
     * 工程名称
     */
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
    private BigDecimal maxRange;
    /**
     * 最大载重量
     */
    private BigDecimal maxWeight;
    /**
     * 标准高度
     */
    private BigDecimal standardHeight;
    /**
     * 额定力矩
     */
    private BigDecimal fixMoment;
    /**
     * 风速
     */
    private BigDecimal windSpeed;
    /**
     * 倾角
     */
    private BigDecimal tiltAngle;
    /**
     * 在线状态
     */
    private Integer isOnline;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 位置
     */
    private String placePoint;
    /**
     * 备注
     */
    private String comments;
    /**
     * 组织结构
     */
    private Integer orgId;
    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * GPRS
     */
    private Integer gprs;



    /**
     * 删除标志
     */
    private Integer isDel;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 创建人
     */
    private Integer createUser;
    /**
     * 创建人名称
     */

    private String createUserName;
    /**
     * 修改日期
     */
    private Date modifyTime;
    /**
     * 修改人
     */
    private Integer modifyUser;


}
