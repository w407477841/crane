package com.xywg.attendance.modular.command.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author z
 * @since 2019-04-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("buss_device_command")
public class BussDeviceCommand extends Model<BussDeviceCommand> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("device_sn")
    private String deviceSn;
    private Integer type;
    private Integer state;
    @TableField("process_date")
    private Date processDate;
    private String description;
    @TableField("create_date")
    private Date createDate;
    @TableField("create_user")
    private String createUser;
    @TableField("update_date")
    private Date updateDate;
    @TableField("update_user")
    private String updateUser;
    private String remark;
    @TableField("is_del")
    private Integer isDel;
    private String uuid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public BussDeviceCommand() {
    }

    public BussDeviceCommand(String deviceSn, Integer type, Integer state, Date processDate, String description, Date createDate, Integer isDel, String uuid) {
        this.deviceSn = deviceSn;
        this.type = type;
        this.state = state;
        this.processDate = processDate;
        this.description = description;
        this.createDate = createDate;
        this.isDel = isDel;
        this.uuid = uuid;
    }
}
