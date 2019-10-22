package com.xywg.equipmentmonitor.modular.device.vo;

import com.xywg.equipmentmonitor.modular.device.model.ProjectSpray;
import lombok.Data;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 11:14
 */
@Data
public class ProjectSprayVO extends ProjectSpray{
    private String createUserName;
    private String projectName;
    private String statusName;
    private String onlineName;
}
