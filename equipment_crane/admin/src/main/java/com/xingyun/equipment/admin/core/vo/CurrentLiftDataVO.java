package com.xingyun.equipment.admin.core.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:41 2018/8/30
 * Modified By : wangyifei
 */
@Data
public class CurrentLiftDataVO implements Serializable {
    Integer flag;
    String deviceNo;
    String identifier;
    String manufactor;
    String owner;
    Double weight;
    Double height;
    Double speed;
    String frontDoorStatus;
    String backDoorStatus;
    String status;
    String key1;
    String key2;
    String key3;
    Date deviceTime;

}
