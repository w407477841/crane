package com.xingyun.equipment.websocket.rabbitmq.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2019-05-05
 */
@Data
public class MqError {


	private Integer id;
    /**
     * 错误信息
     */
	private String error;
    /**
     * 交换机名
     */
	private String exchange;
    /**
     * 队列名
     */
	private String queue;
    /**
     * 路由规则
     */
	private String routingKey;
    /**
     * 时间
     */
	private Date time;
    /**
     * 数据
     */
	private String message;
    /**
     * 信息标签号
     */
	private Long deliveryTag;


}
