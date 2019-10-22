package com.xywg.equipmentmonitor.modular.station.vo;

import com.xywg.equipmentmonitor.core.util.Convert;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:07 2019/3/27
 * Modified By : wangyifei
 */
@Data
public class ProjectDeviceVO {

   private Integer id;
   private String deviceNo;
   private String type;

   public static   ProjectDeviceVO convert(Map<String,Object> map){

       ProjectDeviceVO projectDeviceVO = new ProjectDeviceVO();
       projectDeviceVO.setDeviceNo((String) map.get("deviceNo"));
       if(projectDeviceVO.getDeviceNo().substring(6,8).equalsIgnoreCase("0c")){
           projectDeviceVO.setType("主基站");
       }else if(projectDeviceVO.getDeviceNo().substring(6,8).equalsIgnoreCase("0e")){
           projectDeviceVO.setType("子基站");
       }
       projectDeviceVO.setId((Integer) map.get("id"));
       return projectDeviceVO;
   }

}
