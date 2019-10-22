package com.xywg.equipmentmonitor.modular.station.dto;

import lombok.Data;

import java.util.Date;

@Data
public class WeightAndMomentVO {

    private float weight;
    private float moment;
    private Date createTime;

}
