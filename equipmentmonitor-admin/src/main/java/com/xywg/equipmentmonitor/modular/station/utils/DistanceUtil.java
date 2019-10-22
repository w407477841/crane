package com.xywg.equipmentmonitor.modular.station.utils;

import cn.hutool.json.JSONUtil;
import com.xywg.equipmentmonitor.modular.station.dto.Coordinate;
import com.xywg.equipmentmonitor.modular.station.dto.GpsCoordinate;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:56 2019/5/20
 * Modified By : wangyifei
 */
public class DistanceUtil {

    private static double rad(double d) {
        //角度转换成弧度
        return d * Math.PI / 180.00;
    }
    /**
     * 根据经纬度计算两点之间的距离（单位米）
     * */
    public static double algorithm(double longitude1, double latitude1, double longitude2, double latitude2) {
// 纬度
        double Lat1 = rad(latitude1);

        double Lat2 = rad(latitude2);
//两点纬度之差
        double a = Lat1 - Lat2;
//经度之差
        double b = rad(longitude1) - rad(longitude2);

        double s = 2 * Math.asin(Math
//计算两点距离的公式
                .sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));
//弧长乘地球半径（半径为米）
        s = s * 6378137.0;
//精确距离的数值
        s = Math.round(s * 10000d) / 10000d;

        return s;

    }

    /**
     *
     * @param o 原点
     * @param x 测量到的点
     * @return
     */
    public static Coordinate toCoordinate(GpsCoordinate o, GpsCoordinate x){
        //原点 o{longitude:120.923132,latitude:31.982946}
        //测算点 x{longitude:120.923608,latitude:31.983742}
        //南北向 { longitude:o.longitude,latitude:x.latitude }
        //东西向 ,{longitude:x.longitude,latitude:o.latitude}

        double ylength  = algorithm(o.getLongitude(),o.getLatitude(),o.getLongitude(),x.getLatitude());
        if(o.getLatitude()-x.getLatitude()>0){
            ylength = -ylength;
        }

        double xlength  = algorithm(o.getLongitude(),o.getLatitude(),x.getLongitude(),o.getLatitude());
        if(o.getLongitude()-x.getLongitude()>0){
            xlength = -xlength;
        }
        Coordinate coordinate = new Coordinate(xlength,ylength);
        return coordinate;
    }

    public static void main(String[] args) {
        //根据两点间的经纬度计算距离，单位：km
        GpsCoordinate o = new GpsCoordinate(120.923132,31.982946);
        GpsCoordinate x = new GpsCoordinate(120.922274,31.983302);
        //百度API算出来是{x=80.92,y=39.59}
        System.out.println(JSONUtil.toJsonStr(toCoordinate(o,x)));
    }


}
