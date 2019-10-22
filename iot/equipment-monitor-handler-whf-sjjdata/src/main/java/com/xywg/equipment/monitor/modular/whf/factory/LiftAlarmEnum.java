package com.xywg.equipment.monitor.modular.whf.factory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:42 2018/8/24
 * Modified By : wangyifei
 */
public enum LiftAlarmEnum {
    //正常
    ZHENGCHANG(0,"正常"),
    //重量报警
    ZHONGLIANG_BAOJING(1,"重量报警"),
    //重量预警
    ZHONGLIANG_YUJING(2,"重量预警"),
    //速度报警
    SUDU_BAOJING(3,"速度报警"),
    //前门报警
    QIANMENG_BAOJING(4,"前门报警"),
    //后门报警
    HOUMEN_BAOJING(5,"后门报警"),
    //未知
    WEIZHI(999,"未知")
    ;
    private int status;
    private String msg;

     LiftAlarmEnum(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }


    public String getMsg() {
        return msg;
    }

    public static LiftAlarmEnum getAlarmEnnum(int status){

         for(LiftAlarmEnum alarmEnum:LiftAlarmEnum.values()){
             if(alarmEnum.getStatus() == status){
                 return alarmEnum;
             }
         }
         return  WEIZHI;
    }


}
