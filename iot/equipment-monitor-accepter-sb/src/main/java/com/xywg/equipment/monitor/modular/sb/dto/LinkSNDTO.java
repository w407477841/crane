package com.xywg.equipment.monitor.modular.sb.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:35 2018/10/23
 * Modified By : wangyifei
 */
@Data
public class LinkSNDTO {

    private String  data;
    private Date now;
    public static   LinkSNDTO  factory(String sn){
        LinkSNDTO linkSNDTO = new LinkSNDTO();
        linkSNDTO.setNow(new Date());
        linkSNDTO.setData(sn);
        return linkSNDTO;
    }
}
