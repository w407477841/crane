package com.xingyun.equipment.admin.modular.device.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 18:39 2019/5/6
 * Modified By : wangyifei
 */
@Data
public class LiftAPIVO {

    Integer dataStatus;

    /**
     * 设备编号
     */

    private String deviceNo;

    /**
     * 时间
     */

    private Date deviceTime;


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

    private String frontDoorStatus;
    /**
     * 后门状态
     */

    private String backDoorStatus;

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
    //姓名
    private String key1;
    //id
    private String key2;
    //身份证
    private String key3;

}
