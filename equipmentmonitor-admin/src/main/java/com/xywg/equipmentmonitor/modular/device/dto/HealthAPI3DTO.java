package com.xywg.equipmentmonitor.modular.device.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:54 2018/11/26
 * Modified By : wangyifei
 */
@Data
public class HealthAPI3DTO {

    private Integer id;
    private String code;
    private String info ;
    private String deviceTime;
    private String name;
    private String phone;


}
