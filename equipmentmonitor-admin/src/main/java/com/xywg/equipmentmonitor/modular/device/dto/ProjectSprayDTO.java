package com.xywg.equipmentmonitor.modular.device.dto;

import com.xywg.equipmentmonitor.modular.device.model.ProjectSpray;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 20:56
 */
@Data
public class ProjectSprayDTO {
    private Integer id;
    private List<ProjectSpray> spray;
}
