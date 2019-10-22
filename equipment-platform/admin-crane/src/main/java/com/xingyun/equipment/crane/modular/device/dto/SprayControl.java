package com.xingyun.equipment.crane.modular.device.dto;

import com.xingyun.equipment.crane.modular.device.model.ProjectSpray;
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
