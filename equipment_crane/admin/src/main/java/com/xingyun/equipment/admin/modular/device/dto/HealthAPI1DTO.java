package com.xingyun.equipment.admin.modular.device.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:22 2018/11/23
 * Modified By : wangyifei
 */
@Data
public class HealthAPI1DTO {

    private HealthInfo info ;
    private List<String> time;
    private List<BigDecimal> temperatures;
    private List<BigDecimal> pressures;
    private List<Integer> oxygen;
    private List<HealthLocation> locations;
    private String code;
}
