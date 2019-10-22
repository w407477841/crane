package com.xywg.equipmentmonitor.core.dto;


import lombok.Data;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:33 2019/7/5
 * Modified By : wangyifei
 */
@Data
public class BindHelmetVO {

        private String uuid;
        /**新增1 修改2 删除3*/
        private Integer type;
        private String deviceNo;

    public BindHelmetVO() {
    }

    public BindHelmetVO(String uuid, Integer type, String deviceNo) {
        this.uuid = uuid;
        this.type = type;
        this.deviceNo = deviceNo;
    }
}
