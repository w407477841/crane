package com.xingyun.equipment.simpleequipment.core;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:05 2019/7/30
 * Modified By : wangyifei
 */
public class Const {

    //1塔吊
    public static final int DEVICE_TYPE_FLAG_CRANE = 1;
    //2升降机
    public static final int DEVICE_TYPE_FLAG_LIFT = 2;
    //3扬尘
    public static final int DEVICE_TYPE_FLAG_DUST = 3;
    //星云网格协议头开始位标识
    public static final String  XYWG_PROTOCOL_HEAD  ="FFFFFE";
    //设备在线
    public static final int DEVICE_STATE_ON_LINE=1;
    //设备离线
    public static final int DEVICE_STATE_OFF_LINE=0;

    public static final int DEVICE_STATE_YUJING = 2 ;
    public static final int DEVICE_STATE_BAOJING = 3 ;
    public static final int DEVICE_STATE_CHAOBIAO = 4 ;

    /**
     * AQ（Package type）：表示数据包是请求包、应答包，1：请求包
     */
    public static final String DATA_PACKEAGE_REQUEST="01";

    /**
     * AQ（Package type）：表示数据包是请求包、应答包，0：应答包
     */
    public static final String DATA_PACKAGE_RESPONSE="00";


    /**
     * 下线
     */
    public static final int TERMINAL_DOWN = Integer.MAX_VALUE;
    /**
     *  登录
     */
    public static final int LOGIN = 0x10;
    /**
     * 定位、健康
     */
    public static final int POSITION_HEALTH = 0x30;
    /**
     * // 定位
     */
    public static final int POSITION = 0x07;
    /**
     * 健康
     */
    public static final int HEALTH = 0x0f;

    /**
     * 断电离线
     */
    public static final String OFFLINE_REASON_NOPOWER = "断电离线";
    /**
     * 正常离线
     */
    public static final String OFFLINE_REASON_NORMAL = "正常离线";
    /**
     * 网络异常
     */
    public static final String OFFLINE_REASON_NETWORK = "网络异常";
    /**
     * 服务器重启
     */
    public static final String OFFLINE_REASON_SERVER_REBOOT = "服务器重启";

    /**
     * 在线报警状态KEY
     * @param header
     * @param sn
     * @param index
     * @return
     */
    public static String getAlarmSatusKey(String header,String sn,int index){
        return header+":crane:alarm_status:"+sn+":"+index;
    }

    /**
     * 离线报警状态KEY
     * @param header
     * @param sn
     * @param index
     * @return
     */
    public static String getOfflineAlarmSatusKey(String header,String sn,int index){
        return header+":crane:offline:alarm_status:"+sn+":"+index;
    }

    /**
     * 在线 最后一个报警信息KEY
     * @param header
     * @param sn
     * @param index
     * @return
     */
    public static String getLastAlarmKey(String header,String sn,int index){
        return header+":crane:last_alarm:"+sn+":"+index;
    }
    /**
     * 离线 最后一个报警信息KEY
     * @param header
     * @param sn
     * @param index
     * @return
     */
    public static String getOfflineLastAlarmKey(String header,String sn,int index){
        return header+":crane:offline:last_alarm:"+sn+":"+index;
    }

    /**
     * 当天统计缓存 key
     * @param header
     * @param sn
     * @return
     */
    public static String getStatisticsKey(String header,String sn){
        return header+":crane:statistics:"+sn;
    }

}
