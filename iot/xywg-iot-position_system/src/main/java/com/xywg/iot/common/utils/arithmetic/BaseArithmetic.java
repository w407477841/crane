package com.xywg.iot.common.utils.arithmetic;

import com.xywg.iot.netty.models.Coordinate;
import com.xywg.iot.netty.models.Round;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:02 2019/3/6
 * Modified By : wangyifei
 */
public abstract class BaseArithmetic {
    /**执行算法*/
    public  abstract Coordinate exe(List<Round> rounds);
    /**使用斜边*/

    protected final boolean useHypotenuse;

    public BaseArithmetic(Boolean useHypotenuse) {
        this.useHypotenuse = useHypotenuse;
    }

    public static BaseArithmetic factory(Class<? extends  BaseArithmetic> clazz,boolean useHypotenuse) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor con = clazz.getConstructor(Boolean.class);
        Object  o =  con.newInstance(useHypotenuse);
         return   (BaseArithmetic)o;

    }

    protected List<Round>  hypotenuses(List<Round> rounds){

        List<Round> temps = new ArrayList<>();
        if(!this.useHypotenuse){
            for (Round round:rounds){
                Round temp = round;
                temp.setR(Math.sqrt(Math.abs(Math.pow(round.getR(),2)-Math.pow(0.15,2))));
                temps.add(temp);
            }
            rounds = temps;
        }

        return rounds;
    }

}
