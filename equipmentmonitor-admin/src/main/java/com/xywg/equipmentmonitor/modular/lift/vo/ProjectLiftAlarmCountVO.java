package com.xywg.equipmentmonitor.modular.lift.vo;

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
public class ProjectLiftAlarmCountVO {

    private static final long serialVersionUID = 1L;
    /**
     *报警数量 
     */
    private Integer alarm;
    /**
     *预警数量 
     */
    private Integer earlyWarning;
}
