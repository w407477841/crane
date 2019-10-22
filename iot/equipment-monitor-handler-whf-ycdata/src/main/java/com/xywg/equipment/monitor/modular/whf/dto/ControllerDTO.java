package com.xywg.equipment.monitor.modular.whf.dto;

import lombok.Data;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:09 2018/11/19
 * Modified By : wangyifei
 */
@Data
public class ControllerDTO {

    private String sn;
    private String type;

    private String cmd;

public  static ControllerDTO factory(String sn, String cmd, String type){

    ControllerDTO controllerDTO  =new ControllerDTO();
    controllerDTO.setSn(sn);
    controllerDTO.setCmd(cmd);
    controllerDTO.setType(type);
    return controllerDTO;

}

}
