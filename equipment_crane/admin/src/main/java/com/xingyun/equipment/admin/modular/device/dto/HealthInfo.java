package com.xingyun.equipment.admin.modular.device.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:12 2018/11/26
 * Modified By : wangyifei
 */
@Data
public class HealthInfo {

    private int maozi;
    private String inTime;
    private String outTime;
    private String code;
    private Double temperature;
    private Double pressure;
    private Double oxygen;

}
