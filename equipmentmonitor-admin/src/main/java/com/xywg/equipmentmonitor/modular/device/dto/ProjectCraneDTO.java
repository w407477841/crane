package com.xywg.equipmentmonitor.modular.device.dto;

import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCraneVideo;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectCraneVO;
import lombok.Data;
import java.util.List;

/**
 * @Description:
 * @Author xieshuaishuai
 * @Date Create in 2018/8/23 17:46
 */
@Data
public class ProjectCraneDTO{
    private ProjectCraneVO crane;
    private List<ProjectCraneVideo> videos;
}
