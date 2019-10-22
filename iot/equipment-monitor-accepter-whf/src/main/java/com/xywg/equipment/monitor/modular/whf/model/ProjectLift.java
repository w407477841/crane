package com.xywg.equipment.monitor.modular.whf.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2018-08-22
 */
@Data
public class ProjectLift {

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
     * 最大载重量
     */
	private BigDecimal maxWeight;
    /**
     * 最大载人数
     */
	private Integer maxPeople;
    /**
     * 标准高度
     */
	private BigDecimal standardHeight;
    /**
     * 运行速度
     */
	private BigDecimal speed;
    /**
     * 在线状态
     */
	private Integer isOnline;
    /**
     * 状态
     */
	private Integer status;
    /**
     * 备注
     */
	private String comments;
	private String deviceNo;

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
     * 修改日期
     */
	private Date modifyTime;
    /**
     * 修改人
     */
	private Integer modifyUser;
    /**
     * 组织结构
     */
	private Integer orgId;





}
