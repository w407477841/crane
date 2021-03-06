package com.xywg.equipment.monitor.iot.netty.device.model;

import com.xywg.equipment.monitor.iot.modular.spray.model.ProjectSpray;
import lombok.Data;

import java.util.List;

/**
 * @author hjy
 * @date 2019/4/3
 */
@Data
public class SprayControl {

    private List<ProjectSpray> list;

    /**
     * 00关闭喷淋 ,01 打开喷淋
     */
    private String type;


}
