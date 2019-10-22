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
public class MqQueue  {


	private Integer id;
    /**
     * 队列名称
     */
	private String name;
    /**
     * 描述
     */
	private String description;


}
