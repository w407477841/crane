package com.xywg.equipmentmonitor.modular.device.vo;

import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import lombok.Data;

/**
 * @Description:
 * @Author xieshuaishuai
 * @Date Create in 2018/8/22 18:30
 */
@Data
public class ProjectCraneVO extends ProjectCrane{

    private String projectName;

    private String createUserName;
    /**
     * 判断在线离线状态
     */
    private String statusName;
}
