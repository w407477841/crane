package com.xywg.iot.common.utils;

import com.xywg.iot.common.utils.arithmetic.BaseArithmetic;
import com.xywg.iot.common.utils.arithmetic.CentroidArithmetic;
import com.xywg.iot.netty.models.Coordinate;
import com.xywg.iot.netty.models.Round;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:57 2019/2/18
 * Modified By : wangyifei
 */
public class GisMapUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(GisMapUtil.class);

public static void main(String [] args){

//key:1:170:37:1  data:Round(x=7.2, y=11.0, r=7.22)
//key:1:170:37:3  data:Round(x=0.0, y=0.0, r=4.64)

    Round r1=new Round(7.2,11,5.09);
    Round r2=new Round(7.2,0,5.19);
    Round r3=new Round(0,0,6.19);
    Round r4=new Round(2.4,10.0,8.7);
    List<Round> rounds = new ArrayList<>();

    rounds.add(r1);
    rounds.add(r2);
    rounds.add(r3);
    rounds.add(r4);
    //使用质心法计算

    BaseArithmetic centroidArithmetic = null;
    try {
        centroidArithmetic = BaseArithmetic.factory(CentroidArithmetic.class,true);
    } catch (NoSuchMethodException e) {
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    } catch (InvocationTargetException e) {
        e.printStackTrace();
    } catch (InstantiationException e) {
        e.printStackTrace();
    }
    Coordinate avgZxl =    centroidArithmetic.exe(rounds);
    System.out.println("质心法"+ avgZxl.toString());

    //加权质心算法
//    Coordinate avgZxl  = dojqzxf(rounds);
// System.out.println(avgZxl.toString());

//    Coordinate zx1= tcl(r1, r2, r3);
//    System.out.println(zx1.getX());
//    System.out.println(zx1.getY());






}



}
