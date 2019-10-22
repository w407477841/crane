package com.xywg.equipmentmonitor.modular.station.vo;

import com.xywg.equipmentmonitor.modular.station.model.ProjectMap;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:21 2019/3/20
 * Modified By : wangyifei
 */
@Data
public class ProjectMapVO {

    private String path;
    private BigDecimal xZhou;
    private BigDecimal yZhou;

    public static ProjectMapVO convert(ProjectMap projectMap){

        ProjectMapVO projectMapVO = new ProjectMapVO();
        projectMapVO.setPath(projectMap.getPath());
        projectMapVO.setXZhou(projectMap.getXZhou());
        projectMapVO.setYZhou(projectMap.getYZhou());
        return projectMapVO;
    }

}
