package com.xywg.attendance.modular.handler.model;

import lombok.Data;

import java.util.List;

/**
 * @author hjy
 * @date 2019/4/16
 */
@Data
public class Command {
    /**
     * 设备序列号
     */
    private List<String> deviceSnList;

    /**
     * 命令类型 1重启设备 2清空考勤记录 3删除所有人员 4手动下发人员信息 5移除个别人员
     */
    private Integer commandType;


    private List<WorkerInfo> workerInfoList;


}
