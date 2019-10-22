package com.xywg.iot.common.utils.arithmetic;

import com.xywg.iot.common.exceptions.round.ConcentricCirclesException;
import com.xywg.iot.common.exceptions.round.IncludeException;
import com.xywg.iot.common.exceptions.round.IncrossException;
import com.xywg.iot.netty.models.Coordinate;
import com.xywg.iot.netty.models.Length;
import com.xywg.iot.netty.models.Round;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author : wangyifei
 * Description 质心算法
 * 1.三圆两两相交，根据焦点形成的较小三角形，求出质心
 * 2.当三圆都相离时，舍弃
 * 3.当三圆中有两队相交时，较近的交点形成的直线的中心
 * Date: Created in 9:05 2019/3/6
 * Modified By : wangyifei
 */
@Slf4j
@SuppressWarnings("all")
public class CentroidArithmetic extends BaseArithmetic {
    /**最大AP数*/
    private static final  int MAX_AP_COUNT = 4 ;
    /**两个待计算的基站，最大可接受距离*/
    private static final double MAX_ACCEPTABLE_LEVEL = 30;

    private static final Logger LOGGER = LoggerFactory.getLogger(CentroidArithmetic.class);

    public CentroidArithmetic(Boolean useHypotenuse) {
        super(useHypotenuse);
    }

    @Override
    public  Coordinate exe(List<Round> rounds) {
        rounds = hypotenuses(rounds);
        //按半径排序
        Collections.sort(rounds);
//        if(rounds.get(0).getR()<1){
//            Coordinate coordinate = new Coordinate();
//            coordinate.setX(rounds.get(0).getX());
//            coordinate.setY(rounds.get(0).getY());
//            LOGGER.info("离基站特近 直接取改点");
//            return coordinate;
//        }


        // 截取 rssi最小的 几个AP

//        if(rounds.size()>MAX_AP_COUNT){
//            //使用 第二到第五个
//            // rounds = rounds.subList(1,MAX_AP_COUNT);
//            //使用前4个
//            rounds = rounds.subList(0,MAX_AP_COUNT-1);
//        }else{
//            throw new RuntimeException("数据太少了");
//        }

        rounds =  validError(rounds,0);

        System.out.println("############用于计算的列表  开始############");
        rounds.forEach(item->{
            System.out.println(item.toString());
        });
        System.out.println("############用于计算的列表   结束############");
        Coordinate coordinate =    avgZxl(rounds);
        return coordinate;
    }

    /**
     * 计算两点长度
     * @param x1
     * @param x2
     * @return
     */
    public static double  length(Round x1,Round x2){
        double length =Math.sqrt(Math.pow(x1.getX() - x2.getX(), 2) + Math.pow(x1.getY() - x2.getY(), 2));
      //  log.info("点1{},点2{}  距离{}",x1.toString(),x2.toString(),length);
        return  length;
    }
    public static List<Round> validError(List<Round> rounds,int count){
        // 待检验的4个数据
        if(count>2){
            throw new RuntimeException("检验了次数超过了{"+count+"}次，无意义");
        }
        if((MAX_AP_COUNT-1)>rounds.size()){
            throw new RuntimeException("检验了所有数据，未找到符合条件的4个点");
        }
        boolean error = false;
        /** 存在超过1次的
         * */
        boolean hasMoreThan1 = false;


        List<Round> awaitValidList = rounds.subList(0,MAX_AP_COUNT-1);
        // 打印下待检验的 4个点
        log.info("##############待测列表################");
        awaitValidList.forEach((item)->{
            log.info(item.toString());
        });
        // key:点序号,value: 错误数量
        Map<Integer,Integer> errorMap = new HashMap<>(MAX_AP_COUNT-1);
        // key:点序号,value: 距离其他点长度
        Map<Integer,Map<Integer,Double>>   lengthMap = new HashMap<>(MAX_AP_COUNT-1);
        // 可能要进待删点的 下标及长度
        String mayAwaitDel = "-1,0" ;

        //待删除的集合
        List<Integer> awaitDelList = new ArrayList<>(MAX_AP_COUNT-1);
        // 组装每个坐标到其坐标 超长的数量
         for(int i = 0; i<MAX_AP_COUNT-1 ; i++){
            for(int j = 0; j<MAX_AP_COUNT-1 ; j++){
                if(i!=j){
                    Double length = length(awaitValidList.get(i),awaitValidList.get(j));
                    if(length>MAX_ACCEPTABLE_LEVEL){
                       Integer errorCount =  errorMap.getOrDefault(i,0);
                        error = true;
                        // 错误数量+1
                        errorMap.put(i,errorCount+1);
                        // 存放i点到j点距离
                        Map<Integer,Double> map =  lengthMap.getOrDefault(i,new HashMap<>());
                        map.put(j,length);
                        lengthMap.put(i,map);


                        if(errorMap.get(i)>1){
                            // 为了实现 三点间 ，存在一点到其他两点都大于 MAX_ACCEPTABLE_LEVEL 的情况
                            hasMoreThan1 = true;
                        }
                    }else{
                        Integer errorCount =  errorMap.getOrDefault(i,0);
                        errorMap.put(i,errorCount);
                    }
                }
            }
        }
          if(!error){
                return awaitValidList;
          }

         for(Map.Entry<Integer,Integer> entry:errorMap.entrySet()){
             if(hasMoreThan1){
                 //3个点 单点距离大于 MAX_ACCEPTABLE_LEVEL 数量超过 1 次，即两次的时候
                 // 则只需删除 2次的点
                 if(entry.getValue() > 1){
                     // 超过了一个，这个点肯定要去掉了;
                     awaitDelList.add(entry.getKey());
                 }
             }else{
                 // 3个点 单点距离大于 MAX_ACCEPTABLE_LEVEL 数量不超过 1 次
                 if(entry.getValue() >= 1){
                     // 虽然写着 >=1 实际上只能是 ==1 的情况
                      Map<Integer,Double> map = lengthMap.get(entry.getKey());
                      for(Map.Entry<Integer,Double> length:map.entrySet()){
                           if(Double.parseDouble(mayAwaitDel.split(",")[1]) > length.getValue()){
                               // 存放 距离大的
                               mayAwaitDel = length.getKey()+","+length.getValue();
                           }
                      }
                      awaitDelList.add(entry.getKey());
                 }
             }

         }
         if(!"-1,0".equals(mayAwaitDel)){
             // 情况是只有两个点间的距离大于MAX_ACCEPTABLE_LEVEL ， 删除到另一个点较大的一个点
             awaitDelList.add(Integer.parseInt(mayAwaitDel.split(",")[0]));
         }
            Collections.sort(awaitDelList);
            Collections.reverse(awaitDelList);
            awaitDelList.forEach(item->
                log.info("删除了{}",rounds.remove(item.intValue()).toString())
            );
            log.info("去除垃圾点后长度{}",rounds.size());

        return validError(rounds,count+1);
    }

    /**
     * 用于质心法
     * 计算平均质心
     * @param rounds
     */
    public static Coordinate avgZxl(List<Round> rounds){

        List<Coordinate> coordinates =new ArrayList<>();

        //定义长度
        int size = rounds.size();
        if(size>=3){
            //锚点数 大于3
            // 排列组合 C（n,3）
            for(int i=0;i<size-2;i++){
                for(int j=i+1;j<size-1;j++){
                    for (int k=j+1;k<size;k++){

                        try {
                            Coordinate zx1= tcl(rounds.get(i),rounds.get(j),rounds.get(k));
                            if(zx1!=null){
                                coordinates.add(zx1);
                            }
                        }catch (Exception e){
                            LOGGER.info(e.getMessage());
                        }



                    }
                }
            }
        }
        if(coordinates.size()>0){
            return Coordinate.avg(coordinates);
        }
        return null;
    }


    /**
     * 三角形质心定位算法实现
     * Triangle centroid location
     * @param r1    坐标1为圆心,距离为半径
     * @param r2    坐标2为圆心,距离为半径
     * @param r3    坐标3为圆心,距离为半径
     * @return
     */

    public static Coordinate tcl(Round r1, Round r2, Round r3) throws ConcentricCirclesException, IncludeException, IncrossException {
        // 有效交叉点1
        Coordinate p1 = null;
        // 有效交叉点2
        Coordinate p2 = null;
        // 有效交叉点3
        Coordinate p3 = null;
        //计算三点质心
        Coordinate zx=new Coordinate();
        /** 有焦点的圆的对数
         *  如 r1 和 r2 有焦点则 组成1对
         * */
        int count = 0 ;

        List<Coordinate> temp = new ArrayList<>();


        List<Coordinate> jds1 = jd(r1.getX(), r1.getY(), r1.getR(), r2.getX(), r2.getY(), r2.getR());
        if (jds1 != null && !jds1.isEmpty()) {
            // r1,r2交点
            p1 = minJd(jds1,r3);
            if(jds1.size()==1){
                temp.add(jds1.get(0));
                temp.add(jds1.get(0));
            }else{
                temp.add(jds1.get(0));
                temp.add(jds1.get(1));
            }
            p1.setPrecise(true);
            count += 1 ;
        } else {
            LOGGER.info(r1.toString()+"   "+r2.toString()+"  未形成焦点");
            //没有交点定位错误
            p1 =  new Coordinate();
            p1.setPrecise(false);
        }
        // r1,r2交点
        List<Coordinate> jds2 = jd(r1.getX(), r1.getY(), r1.getR(), r3.getX(), r3.getY(), r3.getR());
        if (jds2 != null && !jds2.isEmpty()) {
            p2 = minJd(jds2,r2);
            if(jds2.size()==1){
                temp.add(jds2.get(0));
                temp.add(jds2.get(0));
            }else{
                temp.add(jds2.get(0));
                temp.add(jds2.get(1));
            }
            p2.setPrecise(true);
            count += 1 ;
        } else {
            //没有交点定位错误
            LOGGER.info(r1.toString()+"   "+r3.toString()+"  未形成焦点");
            //没有交点定位错误
            p2 =  new Coordinate();
            p2.setPrecise(false);
        }
        // r1,r2交点
        List<Coordinate> jds3 = jd(r2.getX(), r2.getY(), r2.getR(), r3.getX(), r3.getY(), r3.getR());
        if (jds3 != null && !jds3.isEmpty()) {
            p3 = minJd(jds3,r1);
            if(jds3.size()==1){
                temp.add(jds3.get(0));
                temp.add(jds3.get(0));
            }else{
                temp.add(jds3.get(0));
                temp.add(jds3.get(1));
            }
            p3.setPrecise(true);
            count += 1 ;
        } else {
            //没有交点定位错误
            LOGGER.info(r2.toString()+"   "+r3.toString()+"  未形成焦点");
            //没有交点定位错误
            p3 =  new Coordinate();
            p3.setPrecise(false);
        }
        if(count ==3){
            //有三队焦点
            double  weight = (1/r1.getR()+1/r2.getR()+1/r3.getR())*2;
            double weightA =1/r1.getR()+1/r2.getR();
            double weigthB = 1/r1.getR()+1/r3.getR();
            double weightC = 1/r2.getR()+1/r3.getR();
             ;

            zx.setX((p1.getX()*weightA+p2.getX()*weigthB+p3.getX()*weightC)/weight);
            zx.setY((p1.getY()*weightA+p2.getY()*weigthB+p3.getY()*weightC)/weight);
            zx.setPrecise(true);
            return zx;
        }else if(count==2){
            //有两队焦点
            // 计算距离最小的点
            Length length  =  minPoints(temp);

            zx.setX((length.getPoint1().getX()+length.getPoint2().getX())/2);
            zx.setY((length.getPoint1().getY()+length.getPoint2().getY())/2);
            zx.setPrecise(true);
            return zx;

        }else{
            // 只能用三个基站的质心了
            zx.setX((r1.getX()+r2.getX()+r3.getX())/3);
            zx.setY((r1.getY()+r2.getY()+r3.getY())/3);
            zx.setPrecise(false);
            return zx;
        }
    }

    /***
     * @param r1 圆1半径 ，需要修正的半径
     * @param r2 圆2半径
     * @param d  两圆距离
     * 距离修正
     * @return
     */
    public  static double lengthCorrection( double r1,double r2, double d){
        return r1*(d-r1-r2)/(r1+r2)+r1;
    }

     /**
     * 求两个圆的交点
     * @param x1,y1 圆心1坐标
     * @param r1 半径
     * @param x2,y2 圆心2坐标
     * @param r2 半径
     * @return
     */
    public static List<Coordinate> jd(double x1, double y1, double r1, double x2, double y2, double r2) throws IncrossException, IncludeException, ConcentricCirclesException {


        // 两圆心距离
        double d = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));

        //交点坐标
        List<Coordinate>points  =new ArrayList<Coordinate>();
        Coordinate coor;
        //

        twoRound(d,r1,r2);

        if (d > r1 + r2|| d<Math.abs(r1-r2) ) {
            //相离或内含
            LOGGER.info("相离/内含，进行缩放");
            double  temp_r1 = lengthCorrection(r1,r2,d);
            double temp_r2 = lengthCorrection(r2,r1,d);
            r1  = temp_r1;
            r2  = temp_r2;
        }
        if (d > r1 + r2|| d<Math.abs(r1-r2) ) {
            //相离或内含
            LOGGER.info("还是相离/内含");
            return null;
        }
        else  if (y1 == y2 && x1 != x2) {
            double a = ((r1 * r1 - r2 * r2) - (x1 * x1 - x2 * x2)) / (2 * x2 - 2 * x1);
            if (d == Math.abs(r1 - r2) || d == r1 + r2) {
                // 只有一个交点时\
                coor=new Coordinate(a,y1);
                points.add(coor);
            } else{
                // 两个交点
                double t = r1 * r1 - (a - x1) * (a - x1);
                coor=new Coordinate(a,y1+Math.sqrt(t));
                points.add(coor);
                coor=new Coordinate(a,y1 - Math.sqrt(t));
                points.add(coor);
            }
        } else if (y1 != y2) {
            double k, disp;
            k = (2 * x1 - 2 * x2) / (2 * y2 - 2 * y1);
            // 直线偏移量
            disp = ((r1 * r1 - r2 * r2) - (x1 * x1 - x2 * x2) - (y1 * y1 - y2 * y2)) / (2 * y2 - 2 * y1);
            double a, b, c;
            a = (k * k + 1);
            b = (2 * (disp - y1) * k - 2 * x1);
            c = (disp - y1) * (disp - y1) - r1 * r1 + x1 * x1;
            double disc;
            // 一元二次方程判别式
            disc = b * b - 4 * a * c;
            if (d == Math.abs(r1 - r2) || d == r1 + r2) {
                coor=new Coordinate((-b/(2*a)),k * (-b/(2*a)) + disp);
                points.add(coor);
            } else {
                coor=new Coordinate((((-b) + Math.sqrt(disc)) / (2 * a)),k*(((-b) + Math.sqrt(disc)) / (2 * a))+disp);
                points.add(coor);
                coor=new Coordinate((((-b) - Math.sqrt(disc)) / (2 * a)),k*(((-b) - Math.sqrt(disc)) / (2 * a))+disp);
                points.add(coor);
            }
        }

        return points;
    }

    /** 判断，两圆的情况*/
    public static void twoRound(double d,double r1,double r2) throws ConcentricCirclesException, IncrossException, IncludeException {

        double big;
        double small;

        if(r1>r2){
            big = r1;
            small = r2;
        }else{
            big = r2;
            small = r1 ;
        }
        if(d==0){
            throw new ConcentricCirclesException("同心圆");
        }
        if(d>big-small&&d<big){
            //内交
            //    throw new IncrossException("内交");
        }
//        if(d>0&&d<big-small){
//            //内含
//            throw new IncludeException("内含");
//        }


    }

    /**
     *  返回距离最短的两个点
     * @return
     */
    public static Length minPoints(List<Coordinate> coordinates){
        List<Length> lengths =new ArrayList<>();
        lengths.add(new Length(coordinates.get(0),coordinates.get(2)));
        lengths.add(new Length(coordinates.get(0),coordinates.get(3)));
        lengths.add(new Length(coordinates.get(1),coordinates.get(2)));
        lengths.add(new Length(coordinates.get(1),coordinates.get(3)));

        Collections.sort(lengths);

        return lengths.get(0);
    }


    /**
     *  返回距离近的点
     *  适用于 三圆两两相交
     * @param jds3
     * @param r1
     * @return
     */
    public static Coordinate minJd(List<Coordinate> jds3,Round r1){
        Coordinate p3=null ;
        for (Coordinate jd : jds3) {
            //有交点
            if (Math.pow(jd.getX()-r1.getX(),2) + Math.pow(jd.getY()-r1.getY(),2) <= Math.pow(r1.getR(),2)) {
                p3 = jd;
            }else if(p3!=null){
                if(Math.pow(jd.getX()-r1.getX(),2) + Math.pow(jd.getY()-r1.getY(),2) <= Math.pow(r1.getR(),2)){
                    if(Math.pow(jd.getX()-r1.getX(),2) + Math.pow(jd.getY()-r1.getY(),2)>Math.sqrt(Math.pow(p3.getX()-r1.getX(),2) + Math.pow(p3.getY()-r1.getY(),2))){
                        p3 = jd;
                    }
                }
            }

        }

        if(p3 ==null){
            //说明两个点都在圆外，取距离短的
            if(jds3.size()==1){
                p3 = jds3.get(0);
            }else{
                Coordinate p1 =jds3.get(0);
                Coordinate p2 =jds3.get(1);
                double length1 =  Math.pow(p1.getX()-r1.getY(),2);
                double length2 =  Math.pow(p2.getX()-r1.getY(),2);
                if(length1<length2){
                    p3 = p1;
                }else{
                    p3 = p2 ;
                }
            }

        }
        return p3;
    }
}
