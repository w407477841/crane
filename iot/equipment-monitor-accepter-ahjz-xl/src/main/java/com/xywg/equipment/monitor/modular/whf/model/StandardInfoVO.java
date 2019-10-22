package com.xywg.equipment.monitor.modular.whf.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StandardInfoVO {

    private String deviceNo;
    private BigDecimal noLoadWeightAD;
    private BigDecimal noLoadWeight;
    private BigDecimal loadWeightAD;
    private BigDecimal loadWeight;

    @Override
    public String toString() {
        return "StandardInfoVO{" +
                "noLoadWeightAD=" + noLoadWeightAD +
                ", noLoadWeight=" + noLoadWeight +
                ", loadWeightAD=" + loadWeightAD +
                ", loadWeight=" + loadWeight +
                '}';
    }
}
