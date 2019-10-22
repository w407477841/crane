package com.xywg.iot.netty.server;

import cn.hutool.core.util.StrUtil;
import com.xywg.iot.common.utils.DataUtil;
import com.xywg.iot.common.utils.RedisUtil;
import com.xywg.iot.netty.handler.NettyChannelManage;
import com.xywg.iot.netty.handler.PositionHandler;
import com.xywg.iot.netty.model.DeviceConnectInfo;
import com.xywg.iot.netty.models.CompleteDataPojo;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:59 2019/2/25
 * Modified By : wangyifei
 */
public abstract class BaseAction implements InitializingBean {

     public static List<BaseAction> actions =new ArrayList<>();

    /**
     * 执行方法
     * @param ctx
     * @param dataDomain
     */
    public    void handle(ChannelHandlerContext ctx, CompleteDataPojo dataDomain){
        before( ctx,  dataDomain);
        handleImpl( ctx,  dataDomain);
        after(ctx,dataDomain);
    }

    /**
     * 是否支持 参数传入的指令编号
     * @param code 指令编号
     * @return
     */
    public abstract  boolean supports(String code);

    /**
     *  待实现方法，真正业务逻辑
     * @param ctx
     * @param dataDomain
     */
    public  abstract   void handleImpl(ChannelHandlerContext ctx, CompleteDataPojo dataDomain);

    private void before(ChannelHandlerContext ctx, CompleteDataPojo dataDomain){
        if(!this.supports("0001")){
            DeviceConnectInfo deviceConnectInfo =  ctx.channel().attr(NettyChannelManage.NETTY_CHANNEL_KEY).get();
            if(deviceConnectInfo==null) {
                PositionHandler.responseErrorMessageHandle(ctx,dataDomain.getOri(),"00","0A");
                ctx.close();
            }
        }

    }

    private void after(ChannelHandlerContext ctx, CompleteDataPojo dataDomain){

    }

     @Override
     public void afterPropertiesSet() throws Exception {
          actions.add(this);
     }

    /**
     * 计算校验码
     * @param responsedata
     * @return
     */
    protected static String doCrc(String responsedata){
        responsedata= responsedata.substring(36).replaceAll(" ","");
        String crc= getCRC(DataUtil.hexStringToBytes(responsedata));
        return StrUtil.fillBefore(crc,'0',4);
    }

    /**
     * 替换校验码
     * @param responsedata
     * @param crcCode
     * @return
     */
    protected static String changeCrc(String responsedata,String crcCode){
        //原始校验字符串
        String crcCheckCode = responsedata.replaceAll(" ", "");
        StringBuffer sb = new StringBuffer(crcCheckCode);
        //改变协议头中的AQ    0：应答包；1：请求包
        sb.replace(22, 24, crcCode.substring(2,4));
        //错误类型位置
        sb.replace(20, 22, crcCode.substring(0,2));
        return sb.toString();
    }
    /**
     * 计算CRC16校验码
     *
     * @param bytes
     * @return
     */
    public static String getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return Integer.toHexString(CRC);
    }

    public void print(CompleteDataPojo pojo){
        if(true) {
            Map<String, String> map = pojo.getDataMap();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
        }
    }

    public static synchronized boolean  exists(RedisUtil redisUtil,String key,String stationId,double length,String sn){

        return redisUtil.existsAndSet(key,stationId,length,sn);
        //existsAndSet()
    }

    protected void validLogin(ChannelHandlerContext ctx){
        if(ctx.channel().attr(NettyChannelManage.NETTY_CHANNEL_KEY).get()==null){
            throw new RuntimeException("这是一个未登录的请求");
        }
    }

}
