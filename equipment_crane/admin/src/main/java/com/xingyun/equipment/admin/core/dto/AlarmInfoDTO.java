package com.xingyun.equipment.admin.core.dto;

import com.xingyun.equipment.admin.modular.device.vo.AlarmInfoVO;
import lombok.Data;

import java.util.List;

/***
 *@author :caolj
 *DATE:2018/11/20
 */
@Data
public class AlarmInfoDTO {
    /**
     * 已处理
     */
    private Integer handled;

    private Integer unHandled;
    /**
     * 列表
     */
    List<AlarmInfoVO> list;
    Integer total;
}
