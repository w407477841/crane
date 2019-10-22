package com.xingyun.equipment.simpleequipment.core.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:07 2019/7/30
 * Modified By : wangyifei
 */
@Data
@ConfigurationProperties(prefix = "xywg")
public class XywgProerties {
        /** 最小温度 */
        private Double temperatureMin;
        /**最大温度  */
        private Double temperatureMax;
        /** 最小湿度 */
        private Double humidityMin;
        /** 最大湿度 */
        private Double humidityMax;
        /** 最大pm10 */
        private Double pm10;
        /** 最大pm25 */
        private Double pm25;
        /** 最大噪音 */
        private Double noise;
        /** 风速 */
        private Double windSpeed;

        private Integer nettyPort;


}
