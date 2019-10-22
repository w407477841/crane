package com.xywg.equipmentmonitor.core.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @param <T>
 * @author 请求公共方法
 */
@Data
public class RequestDTO<T> {

    /**
     * 搜索关键字
     */
    private String key;
    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 当前页
     */
    private Integer pageNum;
    /**
     * 组织
     */
    private Integer orgId;

    /**
     * 组织树
     */
    private List<Integer> orgIds;

    /**
     * 删除 使用
     */

    private Serializable id;


    /**
     * 批量删除使用
     */
    private List<? extends Serializable> ids;
    /**
     * 设备号
     */
    private String deviceNo;
    /**
     * 时间
     */
    private String yearMonth;
    /**
     * 项目uuid
     */
    private String uuid;
    /**
     * 报警类型
     */
    private Integer alarmId;

    /**
     * 监控ID
     */
    private Integer monitorId;

    private String type;
//    /**
//     * 项目uuids
//     */
//    private List<? extends Serializable> uuids;
    
    private String uuids;

    private T body;

    private Integer upgradeId;

    private String deviceIds;
    
    private Integer status;

    private String beginDate;
    private String endDate;
    private String columnName;
    private Integer detailId;

    private Integer projectId;

    private String imei;


}
