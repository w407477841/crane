package com.xywg.equipment.monitor.modular.whf.dto;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:31 2019/6/4
 * Modified By : wangyifei
 */
@Data
public class UserInfoDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 身份证
     */
    private String idCard;

    /**
     * 认证时间
     */
    private String authTime;

    /**
     * 正确率
     */
    private Integer accuracy;

    public UserInfoDTO() {
    }

    public UserInfoDTO(String username, String userId, String idCard, Integer accuracy) {
        this.username = username;
        this.userId = userId;
        this.idCard = idCard;
        this.accuracy = accuracy;
        this.authTime = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss");
    }
}
