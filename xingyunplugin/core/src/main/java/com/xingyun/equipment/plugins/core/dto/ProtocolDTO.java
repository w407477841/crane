package com.xingyun.equipment.plugins.core.dto;

import lombok.Data;

import java.util.Map;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:01 2019/7/8
 * Modified By : wangyifei
 */
@Data
public class ProtocolDTO {
    private String sn;
    private Integer command;
    Map<String,Object> data;
}
