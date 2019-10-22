package com.xywg.iot.common.utils.arithmetic;

import com.xywg.iot.netty.models.Coordinate;
import com.xywg.iot.netty.models.Round;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:26 2019/3/6
 * Modified By : wangyifei
 */
public class WeightingArithmetic extends BaseArithmetic{

    /**
     * 加权质心算法
     * 根据文献 等于 6 时精度最高
     */
    private static final int RIGHT = 6;

    public WeightingArithmetic(Boolean useHypotenuse) {
        super(useHypotenuse);
    }

    @Override
    public Coordinate exe(List<Round> rounds) {
       rounds = hypotenuses(rounds);
        //按半径排序
        Collections.sort(rounds);
        //定义长度
        int size = rounds.size();
        if(size>=3){
            //锚点数 大于3
            // 排列组合 C（n,3）
            for(int i=0;i<size-2;i++){
                for(int j=i+1;j<size-1;j++){
                    for (int k=j+1;k<size;k++){

                        if(isTriangle(rounds.get(i),rounds.get(j),rounds.get(k))){
                            return  jqzxf(rounds.get(i),rounds.get(j),rounds.get(k));
                        }

                    }

                }
            }
        }
        return null;
    }

    /**
     * 加权质心算法
     * @param roundA
     * @param roundB
     * @param roundC
     * @return
     */
    public static Coordinate jqzxf(Round roundA,Round roundB ,Round roundC){
        double rightA =  Math.pow(1/roundA.getR(),RIGHT);
        double rightB =  Math.pow(1/roundB.getR(),RIGHT);
        double rightC =  Math.pow(1/roundC.getR(),RIGHT);

        double x = (roundA.getX()*rightA+roundB.getX()*rightB+roundC.getX()*rightC)/(rightA+rightB+rightC);
        double y = (roundA.getY()*rightA+roundB.getY()*rightB+roundC.getY()*rightC)/(rightA+rightB+rightC);
        return new Coordinate(x,y);
    }

    /**
     *  三个锚点是否能构成的三角形
     *
     * @param roundA
     * @param roundB
     * @param roundC
     */
    public static boolean isTriangle(Round roundA,Round roundB ,Round roundC){

        double d1 = length(roundA,roundB);
        double d2 = length(roundB,roundC);
        double d3 = length(roundA,roundC);


        double s0 =  area(d1,d2,d3);

        double s1 = area(d1,roundA.getR(),roundB.getR());

        double s2 = area(d2, roundB.getR(),roundC.getR());

        double s3 = area(d3, roundA.getR(),roundC.getR());
        System.out.println("p="+(d1+d2+d3)/2+" d1="+d1+" d2="+d2+" d3="+d3);
        System.out.println("总面积 ="+ s0);
        System.out.println("小三角形成的总面积 ="+ (s1+s2+s3));

        if(Math.abs(s0-s1-s2-s3)<s0*0.01) {
            return true;
        }

        return false;
    }

    public static double area(double d1,double d2, double d3){
        double p  =  (d1+d2+d3)/2;
        return   Math.sqrt(Math.abs(p*(p-d1)*(p-d2)*(p-d3)));

    }

    /**
     *  两点的距离
     * @param roundA
     * @param roundB
     * @return
     */
    public static double length(Round roundA,Round roundB){
        return  Math.sqrt(Math.pow(roundA.getX() - roundB.getX(), 2) + Math.pow(roundA.getY() - roundB.getY(), 2));
    }




}
