package com.xywg.attendance.modular.business.model;

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
@TableName("buss_device_exception_record")
public class DeviceExceptionRecord extends Model<DeviceExceptionRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("device_sn")
    private String deviceSn;

    @TableField("id_card_type")
    private Integer idCardType;

    @TableField("id_card_number")
    private String idCardNumber;

    private Date time;

    private String photo;

    @TableField("create_date")
    private Date createDate;

    @TableField("create_user")
    private String createUser;

    @TableField("update_date")
    private Date updateDate;

    @TableField("update_user")
    private String updateUser;

    private String remark;

    /**
     * 1设备不存在,2人员不存在,3项目不存在,99其他
     */
    @TableField("exception_type")
    private Integer exceptionType;

    @TableField("is_del")
    private Integer isDel;

    /**
     * 考勤类型
     */
    @TableField("at_Type")
    private Integer atType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DeviceExceptionRecord(String deviceSn, Integer idCardType, String idCardNumber, Date time, String photo, Date createDate, Integer exceptionType, Integer isDel, Integer atType) {
        this.deviceSn = deviceSn;
        this.idCardType = idCardType;
        this.idCardNumber = idCardNumber;
        this.time = time;
        this.photo = photo;
        this.createDate = createDate;
        this.exceptionType = exceptionType;
        this.isDel = isDel;
        this.atType = atType;
    }

    public DeviceExceptionRecord() {
    }
}
