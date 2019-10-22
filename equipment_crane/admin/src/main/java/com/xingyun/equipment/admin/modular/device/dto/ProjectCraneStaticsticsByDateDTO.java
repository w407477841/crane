package com.xingyun.equipment.admin.modular.device.dto;




import com.fasterxml.jackson.annotation.JsonInclude;
import com.xingyun.equipment.admin.modular.device.vo.ProjectCraneStatisticsByDateVO;
import com.xingyun.equipment.admin.modular.device.vo.ProjectCraneVO;
import lombok.Data;

import java.util.List;


/***
 * 日期排序
 *@author :caolj
 *DATE:2019/06/25
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectCraneStaticsticsByDateDTO {
    /**
     * ID
     */
    private Integer id;
    /**
     * 工程id
     */
    private Integer projectId;
    /**
     * 工程名称
     */
    private String projectName;
    /**
     * 施工单位id
     */
    private Integer builder;

    /**
     * 施工单位
     */
    private String builderName;

    /**
     * 日期
     */
    private String workDate;

    private String deviceNo;

    /**
     * 查询类型
     */
    private Integer type;

    private String timeIntervalMin;

    private String timeIntervalMax;

    /**
     * 设备列表
     */
    List<ProjectCraneStatisticsByDateVO> deviceList;

    List<String> dayList;

}
