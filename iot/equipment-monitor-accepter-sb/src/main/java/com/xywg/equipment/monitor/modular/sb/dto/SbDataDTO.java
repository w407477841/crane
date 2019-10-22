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
public class SbDataDTO {

    private String  data;
    /** 反向*/
    private String  addr;
    private Date now;

    public static SbDataDTO factory(String data,String addr){
        SbDataDTO dbDataDTO = new SbDataDTO();
        dbDataDTO.setData(data);
        dbDataDTO.setAddr(addr);
        dbDataDTO.setNow(new Date());
        return dbDataDTO;
    }

}
