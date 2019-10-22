package com.xingyun.equipment.admin.modular.device.vo;

import com.xingyun.equipment.admin.core.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hjy
 * @date 2019/3/27
 * 设备出库
 */
@Data
public class DeviceOutStockVo extends BaseEntity<DeviceOutStockVo> {
    private Integer id;

    private Integer projectId;

    private String deviceNo;

    private String imei;

    private Integer isOnline;

    private Integer status;

    private String comments;

    private Integer currentFlag;

    private Integer stockId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
