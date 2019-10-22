package com.xingyun.equipment.admin.modular.device.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @Description:
 * @Author changmengyu
 * @Date Create in 2018/9/19 18:30
 */
@Data
public class ProjectCraneDetailVO {
    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * 最大幅度            
     */
    private BigDecimal range;
    /**
     * 重量
     */
    private BigDecimal weight;
    /**
     * 标准高度 
     */
    private BigDecimal height;
    /**
     * 力矩 
     */
    private BigDecimal moment;
    /**
     * 回转角度
     */
    private BigDecimal rotaryAngle;
    /**
     * 倾角
     */
    private BigDecimal tiltAngle;
    /**
     * 风速
     */
    private BigDecimal windSpeed;
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

    /**
     * 创建日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deviceTime;

    
 		
    			
    		
    			
    				
    		
    			

}
