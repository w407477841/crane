package com.xywg.equipment.monitor.modular.whf.convert;

import cn.hutool.core.util.StrUtil;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitorDetail;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:14 2018/8/24
 * Modified By : wangyifei
 */
public class MonitorConvert {
  /**
  * @author: wangyifei
  * Description:
  * Date: 14:19 2018/8/24
   * @param
  */

   public static ProjectEnvironmentMonitorDetail convert(ProjectEnvironmentMonitorDetail detail){

       detail.setWindDirection(convertWindDirection(detail.getWindDirection()));
       detail.setWindForce(convertWindForce(detail.getWindSpeed()));
       detail.setNoise(detail.getNoise()/10);
       return detail;
   }

    private static Double convertWindForce(Double windSpeed){
       if(windSpeed>=0&& windSpeed<=0.3){
            return new Double(0);
       }else if(windSpeed>0.3&& windSpeed<=1.5){
            return new Double(1);
       }else if(windSpeed>1.5&&windSpeed<=3.3){
            return new Double(2);
       } else if(windSpeed>3.3&&windSpeed<=5.4){
           return new Double(3);
       } else if(windSpeed>5.4&&windSpeed<=7.9){
           return new Double(4);
       } else if(windSpeed>7.9&&windSpeed<=10.7){
           return new Double(5);
       } else if(windSpeed>10.7&&windSpeed<=13.8){
           return new Double(6);
       } else if(windSpeed>13.8&&windSpeed<=17.1){
           return new Double(7);
       } else if(windSpeed>17.1&&windSpeed<=20.7){
           return new Double(8);
       } else if(windSpeed>20.7&&windSpeed<=24.4){
           return new Double(9);
       } else if(windSpeed>24.4&&windSpeed<=28.4){
           return new Double(10);
       } else if(windSpeed>28.4&&windSpeed<=32.6){
           return new Double(11);
       } else if(windSpeed>32.6&&windSpeed<=36.9){
           return new Double(12);
       }else if(windSpeed>36.9&&windSpeed<=41.4){
           return new Double(13);
       }else if(windSpeed>41.4&&windSpeed<=46.1){
           return new Double(14);
       }else if(windSpeed>46.1&&windSpeed<=50.9){
           return new Double(15);
       }else if(windSpeed>50.9&&windSpeed<=56){
           return new Double(16);
       }else if(windSpeed>56&&windSpeed<=61.2){
           return new Double(17);
       }else{
           return new Double(18);
       }

    }

    private static  String convertWindDirection(String value){
       if( StrUtil.isBlank(value)){
           return null;
       }
        Double k = Double.parseDouble(value);
        if(k>0&&k<90) {
            return "东北";
        }else if(k>90&&k<180){
            return "东南" ;
        }else if(k>180&&k<270) {
            return "西南" ;
        }else if(k>270&&k<360){
            return "西北";
        }else if(k==0) {
            return "北";
        }else if(k==90) {
            return "东";
        }else if(k==180) {
            return "南";
        }else if(k==270) {
            return "西";
        }


        return null;
    }

}
