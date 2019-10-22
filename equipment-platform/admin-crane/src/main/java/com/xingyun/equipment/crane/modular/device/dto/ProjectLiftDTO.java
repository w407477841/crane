package com.xingyun.equipment.crane.modular.device.dto;

import com.xingyun.equipment.crane.modular.device.model.ProjectLift;
import com.xingyun.equipment.crane.modular.device.model.ProjectLiftVideo;
import com.xingyun.equipment.crane.modular.device.vo.ProjectLiftVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author yy
 * @since 2018-08-22
 */
@Data
public class ProjectLiftDTO extends ProjectLift {

    private ProjectLiftVO lift;
    private List<ProjectLiftVideo> videos;
    private BigDecimal speed;
}
