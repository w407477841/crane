package com.xywg.equipmentmonitor.modular.device.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xywg.equipmentmonitor.core.model.BaseEntity;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLift;
import com.xywg.equipmentmonitor.modular.device.model.ProjectLiftVideo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
public class ProjectLiftVO extends ProjectLift {
    private String projectName;

    /**
     * 判断设备在线离线
     */
    private String statusName;
    private List<ProjectLiftVideo> projectLiftVideos;
}
