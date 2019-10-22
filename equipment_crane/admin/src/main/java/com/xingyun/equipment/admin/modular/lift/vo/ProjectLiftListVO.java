package com.xingyun.equipment.admin.modular.lift.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectLiftListVO  {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备编号
     */
    private String deviceNo;
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
     *最大载重量
     */
    private BigDecimal maxWeight;


    /**
     * 最大载人数
     */
    private Integer maxPeople ;
    /**
     * 倾角
     */
    private BigDecimal tiltAngle ;
    /**
     * 重量
     */
    private BigDecimal weight ;
    /**
     * 标准高度  
     */
    private BigDecimal height ;
    /**
     * 人数 
     */
    private Integer people ;
    /**
     * 前门状态
     */
    private String frontDoorStatus;
    /**
     * 后门状态
     */
    private String backDoorStatus;
    /**
     * 标准高度
     */
    private BigDecimal standardHeight;
    /**
     * 速度
     */
    private BigDecimal speed;
    /**
     /**
     * gprs
     */
    private String gprs;
    /**
     * 升降机状态
     */
    private int status;
    /**
     * 项目名
     */
    private String projectName;
   /**
    * 创建人名
    */
    private String createUserName;
    
    /**
     *司机编号
     */
    private Integer driver;
    /**
     * 司机手机号
     */
    private String dirverPhone;
    /**
     * 证件编号
     */
    private String dirverCertificateNo;
    /**
     *司机名字
     */
    private String dirverName;
    
	/**
	 * 创建日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;


}
