package com.xywg.iot.modules.helmet.model;

import lombok.Data;

/**
 * @author hjy
 * @date 2018/11/22
 */
@Data
public class OriginalData {
    /**命令码**/
    private Integer command;
    /**
     * 数据
     */
    private DataDomain dataDomain;


}
