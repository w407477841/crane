package com.xingyun.equipment.crane.core.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:48 2018/8/30
 * Modified By : wangyifei
 */
@Data
public class CurrentCraneDataVO implements Serializable {
    Integer flag;
    String deviceNo;
    String identifier;
    String manufactor;
    String owner;
    Double weight;
    Double height;
    String status;
    Date deviceTime;
    Double rang;
    Double rotaryAngle;
    Double moment;
    String key1;
    String key2;
    String key3;


}
