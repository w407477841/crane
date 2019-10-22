package com.xingyun.equipment.crane.modular.device.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:03 2019/1/21
 * Modified By : wangyifei
 */
@Data
public class LiftDetailDTO {


    /**
     * 设备编号
     */

    private String deviceNo;

    /**
     * 时间
     */

    private Date deviceTime;

    private Date createTime;

    /**
     * 重量
     */

    private BigDecimal weight;
    /**
     * 高度
     */

    private BigDecimal height;
    /**
     * 速度
     */

    private BigDecimal speed;
    /**
     * 人数
     */

    private Integer people;
    /**
     * 前门状态
     */

    private Integer frontDoorStatus;
    /**
     * 后门状态
     */

    private Integer backDoorStatus;
    /**
     * 升降机状态
     */

    private Integer status;
    /**
     * 倾角
     */

    private BigDecimal tiltAngle;
    /**
     * 楼层
     */

    private Integer floor;
    /**
     * 启动楼层
     */

    private Integer floorStart;
    /**
     * 停止楼层
     */

    private Integer floorEnd;

    private Date time;
}
