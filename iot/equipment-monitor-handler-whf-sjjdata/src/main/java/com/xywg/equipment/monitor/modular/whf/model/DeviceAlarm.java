package com.xywg.equipment.monitor.modular.whf.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:53 2018/8/28
 * Modified By : wangyifei
 */
@Data
public abstract class DeviceAlarm<T> extends Model<DeviceAlarm> {

    /**
     * 设备编号
     */
    @TableField("device_no")
    private String deviceNo;
    /**
     * 报警类型
     */
    @TableField("alarm_info")
    private String alarmInfo;

    @TableField("alarm_id")
    private Integer alarmId;

    /**
     * 删除标志
     */
    @TableField("is_del")
    private Integer isDel;


    /**
     * 创建日期
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_user")
    private Integer createUser;
    /**
     * 修改日期
     */
    @TableField("modify_time")
    private Date modifyTime;
    /**
     * 修改人
     */
    @TableField("modify_user")
    private Integer modifyUser;

}
