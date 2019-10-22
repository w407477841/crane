package com.xywg.equipment.monitor.iot.config.rabbitmq.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
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
public class MqExchange  {

	private Integer id;
    /**
     * 交换机名称
     */
	private String name;
    /**
     * 交换机类型
     */
	private String type;
    /**
     * 描述
     */
	private String description;

}
