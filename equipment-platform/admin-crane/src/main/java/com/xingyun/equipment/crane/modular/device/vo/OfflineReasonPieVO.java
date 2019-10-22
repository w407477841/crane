package com.xingyun.equipment.crane.modular.device.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 18:38 2019/6/28
 * Modified By : wangyifei
 */
@Data
public class OfflineReasonPieVO {

    String reason;
    Integer account;

    public OfflineReasonPieVO(String reason, Integer account) {
        this.reason = reason;
        this.account = account;
    }

    public OfflineReasonPieVO() {
    }
}
