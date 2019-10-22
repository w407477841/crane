package com.xywg.iot.netty.models;

import lombok.Data;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:41 2019/3/5
 * Modified By : wangyifei
 */
@Data
public class Length implements Comparable<Length>{

   private Coordinate point1;
   private Coordinate point2;

    public Length(Coordinate point1, Coordinate point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    @Override
    public int compareTo(Length o) {

        double d = length(this.getPoint1(),this.getPoint2())-length(o.getPoint1(),o.getPoint2());
        if(d>0) {return 1;}
        else if(d==0){return 0;}
        else {return -1;}
    }

    public static double length(Coordinate roundA,Coordinate roundB){
        return  Math.sqrt(Math.pow(roundA.getX() - roundB.getX(), 2) + Math.pow(roundA.getY() - roundB.getY(), 2));
    }

}
