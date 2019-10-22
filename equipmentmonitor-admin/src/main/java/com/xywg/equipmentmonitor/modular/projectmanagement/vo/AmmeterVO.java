package com.xywg.equipmentmonitor.modular.projectmanagement.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:51 2018/9/18
 * Modified By : wangyifei
 */
@Data
public class AmmeterVO  implements Serializable {
    Double current;
    String deviceTime;
}
