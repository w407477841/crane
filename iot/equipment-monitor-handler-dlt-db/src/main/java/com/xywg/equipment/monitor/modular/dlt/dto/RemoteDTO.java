package com.xywg.equipment.monitor.modular.dlt.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
