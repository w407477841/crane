package com.xingyun.equipment.crane.modular.lift.vo;

import java.util.List;

import com.xingyun.equipment.crane.modular.lift.model.ProjectLiftInfo;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author changmengyu
 * @since 2018-08-26
 */
@Data
public class ProjectLiftInfoVO {

    private static final long serialVersionUID = 1L;

    private List<ProjectLiftInfo> liftList;
    private Integer total;
}
