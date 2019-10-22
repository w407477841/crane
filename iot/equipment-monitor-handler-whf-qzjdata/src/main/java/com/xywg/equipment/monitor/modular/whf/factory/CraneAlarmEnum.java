package com.xywg.equipment.monitor.modular.whf.factory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:39 2018/8/24
 * Modified By : wangyifei
 */
public enum CraneAlarmEnum {
    //正常
    ZHENGCHANG(0,"正常"),
    //起重量报警
    QINGZHONGLIANG_BAOJING(1,"起重量报警"),
    //起重量预警
    QINGZHONGLIANG_YUJING(2,"起重量预警"),
    //幅度向内报警
    FUDUXIANGNEI_BAOJING(3,"幅度向内报警"),
    //幅度向内预警
    FUDUXIANGNEI_预警JING(4,"幅度向内预警"),
    //幅度向外报警
    FUDUXIANGWAI_BAOJING(5,"幅度向外报警"),
    //幅度向外预警
    FUDUXIANGWAI_YUJING(6,"幅度向外预警"),
    //幅度向上报警
    FUDUXIANGSHANG_BAOJING(7,"高度向上报警"),
    //幅度向上预警
    FUDUXIANGSHANG_YUJING(8,"高度向上预警"),
    //力矩报警
    LIJU_BAOJIGN(9,"力矩报警"),
    //力矩预警
    LIJU_YUJING(10,"力矩预警"),
    //单机防碰撞报警
    DAJIFANGPENGZHUANG_BAOJING(11,"单机防碰撞报警"),
    //单机防碰撞预警
    DAJIFANGPENGZHUANG_YUJING(12,"单机防碰撞预警"),
    //多机防碰撞报警
    DUOJIFANGPENGZHUANG_BAOJING(13,"多机防碰撞报警"),
    //多机防碰撞预警
    DUOJIFANGPENGZHUANG_YUJING(14,"多机防碰撞预警"),
    //风速报警
    FENGSU_BAOJING(15,"风速报警"),
    //倾角报警
    QINJIAO_BAOJING(16,"倾角报警"),

    //未知
    WEIZHI(999,"未知");


    private int status;
    private String msg ;

    CraneAlarmEnum(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static CraneAlarmEnum  getAlarm(int status){

        for(CraneAlarmEnum ae : CraneAlarmEnum.values()  ){
            if(ae.getStatus() == status) {
                return ae;
            }
        }
        return WEIZHI;

    }

}
