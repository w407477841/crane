package com.xywg.equipmentmonitor.modular.device.vo;

import lombok.Data;

import java.util.List;

/**
 * @author hjy
 * @date 2019/3/27
 * 设备出库
 */
@Data
public class DeviceOutStock {


    private String tableName;

    private List<DeviceOutStockVo> list;


}
