package com.xywg.equipment.monitor.modular.whf.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:35 2018/10/23
 * Modified By : wangyifei
 */
@Data
public class DbDataDTO {

    private String  data;
    // 反向
    private String  addr;
    private Date now;

    public static DbDataDTO factory(String data,String addr){
        DbDataDTO dbDataDTO = new DbDataDTO();
        dbDataDTO.setData(data);
        dbDataDTO.setAddr(addr);
        dbDataDTO.setNow(new Date());
        return dbDataDTO;
    }

}
