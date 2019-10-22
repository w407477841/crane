package com.xywg.equipment.monitor.modular.whf.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author : wangyifei
 * Description 塔吊
 * Date: Created in 16:52 2019/1/7
 * Modified By : wangyifei
 */
@Data
public class TdDTO {
    /**
     * 坐标x
     */
    private BigDecimal zuobiaox;
    /**
     * 坐标y
     */
    private BigDecimal zuobiaoy;
    /**
     * 前臂长
     */
    private BigDecimal qianbi;
    /**
     * 后臂长
     */
    private BigDecimal houbi;
    /**
     * 他帽高
     */
    private BigDecimal tamaogao;
    /**
     * 塔臂高
     */
    private BigDecimal tabigao;
    /**
     * 最大吊重
     */
    private BigDecimal zuidadiaozhong;
    /**
     * 最大力矩
     */
    private BigDecimal zuidaliju;
    /**
     * 塔基型号
     */
    private String tajixinghao;
    /**
     * 厂家
     */
    private String changjia;
    /**
     * 绞接长度
     */
    private BigDecimal jiaojiechangdu;
    /**
     * 传感器安装状态
     * 00000000
     */
    private String status;


}
