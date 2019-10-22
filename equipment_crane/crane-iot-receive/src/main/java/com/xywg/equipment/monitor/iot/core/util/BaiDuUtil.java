package com.xywg.equipment.monitor.iot.core.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

/**
 * @author hjy
 * @date 2018/12/12
 * 与百度接口相关的工具类
 */
public class BaiDuUtil {
    /**
     * 天气AK
     */
    private static String baiduWeatherAk = "iqpuwcrsViPU60QC7FQ30Ww4RbHlWBGE";

    /**
     * 根据经纬度获取天气预报信息
     *
     * @param placePoint 该参数既可以是城市名称 如:北京  也可以是由经纬度组成的串 如:120.901771,31.991755
     * @param resType    返回参数的类型  json  or   xml
     */
    public static String getWeather(String placePoint, String resType) {
        String url = "http://api.map.baidu.com/telematics/v3/weather?" +
                "location=" + placePoint +
                "&output=" + resType +
                "&ak=" + baiduWeatherAk;
        return HttpUtil.get(url);
    }

    public static void main(String[] args) {
        String sss = getWeather("北京", "json");
        Map map = JSON.parseObject(sss);
        if ("success".equals(map.get("status"))) {
            List list = (List) map.get("results");
            Map ssMap=(Map)list.get(0);
           List weatherList= (List)ssMap.get("weather_data");
            System.out.println(weatherList.get(0));
        }
    }

}
