package com.xywg.equipment.monitor.modular.disaptch.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @author 严鹏
 * @date 2019/7/31
 */
@TableName("t_dispatch_info")
@Data
public class Dispatch {

    /**
     * ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /**
     * 转发的内容
     */
    @TableField("content")
    private String content;

    /**
     * 转发日期
     */
    @TableField("create_time")
    private Date createTime;



}
