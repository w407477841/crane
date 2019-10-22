package com.xingyun.equipment.websocket.rabbitmq.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2019-05-03
 */
@Data
public class MqBinding {


	private Integer id;
    /**
     * 描述
     */
	private String description;
    /**
     * 交换机
     */
	private String exchange;
    /**
     * 队列
     */
	private String queue;
    /**
     * 路由规则
     */
	private String routing;


}
