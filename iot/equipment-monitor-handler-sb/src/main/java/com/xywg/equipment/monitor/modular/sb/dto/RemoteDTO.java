package com.xywg.equipment.monitor.modular.sb.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:39 2018/11/2
 * Modified By : wangyifei
 */
@Data
public class RemoteDTO {

    private String data;
    private String ori ;
    private String send;
    private String deviceNo;
    private Date deviceTime;
    private Date createTime;

}
