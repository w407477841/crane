package com.xywg.iot.modules.helmet.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.iot.common.domain.BaseEntity;
import lombok.Data;

/**
 * @author hjy
 * @date 2018/11/22
 */
@Data
@TableName("t_project_helmet_position_detail")
public class ProjectHelmetPositionDetail extends BaseEntity<ProjectHelmetPositionDetail> {

    @TableField(value = "helmet_id")
    private Integer helmetId;

    private String imei;

    /**
     * 经度
     */
    private Double lng;
    /**
     * 纬度
     */
    private Double lat;
    @TableField(value = "bd_lng")
    private Double baiduLng;

    @TableField(value = "bd_lat")
    private Double baiduLat;


    @TableField(value = "collect_time")
    private String collectTime;

}
