package com.xingyun.equipment.core.dto;

import lombok.Data;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:14 2018/10/26
 * Modified By : wangyifei
 */
@Data
public class RemoteDTO {

    private String topic ;
    private String data ;

public static RemoteDTO factory(String topic,String data){

    RemoteDTO  remoteDTO   =  new RemoteDTO();
    remoteDTO.setData(data);
    remoteDTO.setTopic(topic);
    return remoteDTO;
}

}
