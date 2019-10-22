package com.xywg.iot.netty.models;

import lombok.Data;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:55 2019/2/18
 * Modified By : wangyifei
 */
@Data
public class Round implements Comparable<Round> {

    private double x;
    private double y;
    private double r;

    public Round(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public int compareTo(Round o) {
        double d = this.getR()-o.getR();
        if(d>0) {return 1;}
        else if(d==0){return 0;}
        else {return -1;}
    }
}
