package com.xingyun.equipment.crane.modular.device.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:03 2018/11/30
 * Modified By : wangyifei
 */
@Data
public class OnlineDTO {

    private String uuid;
    private String lastOnlineTime;
    private String deviceNo;

}
