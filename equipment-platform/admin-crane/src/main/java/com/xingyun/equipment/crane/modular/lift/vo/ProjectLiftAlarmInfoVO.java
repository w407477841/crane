package com.xingyun.equipment.crane.modular.lift.vo;

import java.util.List;

import com.xingyun.equipment.crane.modular.lift.model.ProjectLiftAlarmInfo;

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
public class ProjectLiftAlarmInfoVO {

    private static final long serialVersionUID = 1L;

    private List<ProjectLiftAlarmInfo> alarmList;
    
    private Integer total;
}
