package com.xywg.iot.netty.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : wangyifei
 * Description 坐标
 * Date: Created in 9:56 2019/2/18
 * Modified By : wangyifei
 */
@Data
public class Coordinate {
    /**是否精确*/
    private boolean precise;


    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate() {
    }

    public static Coordinate avg(List<Coordinate> coordinateList){
        if(coordinateList==null||coordinateList.size()==0) {return null;}
        /** 精确点数目 */
        List<Coordinate> jqs = new ArrayList<>();
        boolean precise  = true;
        for(Coordinate coordinate :coordinateList){
            if(coordinate.isPrecise()){
                jqs.add(coordinate);
            }
        }
        AtomicReference<Double> sumX = new AtomicReference<>((double) 0);
        AtomicReference<Double> sumY = new AtomicReference<>((double) 0);
        int length = jqs.size();
        if(length == 0){
//            length = coordinateList.size();
//            jqs = coordinateList;
//            precise = false ;
                return null;
        }
        jqs.forEach(item->{
            sumX.updateAndGet(v -> new Double((double) (v + item.getX())));
            sumY.updateAndGet(v -> new Double((double) (v + item.getY())));
        });
        Coordinate coordinate = new Coordinate(sumX.get()/length,sumY.get()/length);
        coordinate.setPrecise(precise);
        return coordinate;
    }

}
