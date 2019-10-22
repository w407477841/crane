package com.xingyun.equipment.crane.modular.device.dto;

import com.xingyun.equipment.crane.modular.device.model.ProjectCrane;
import com.xingyun.equipment.crane.modular.device.model.ProjectCraneFile;
import com.xingyun.equipment.crane.modular.device.model.ProjectCraneVideo;
import com.xingyun.equipment.crane.modular.device.vo.ProjectCraneVO;
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
    private List<ProjectCraneFile> annex;
}
