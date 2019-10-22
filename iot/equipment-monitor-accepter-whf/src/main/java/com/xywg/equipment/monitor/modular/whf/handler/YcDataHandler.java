package com.xywg.equipment.monitor.modular.whf.handler;

import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.ServerProperties;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.netty.Session;
import com.xywg.equipment.monitor.core.util.NettyChannelManage;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.core.util.StrReplace;
import com.xywg.equipment.monitor.modular.whf.dto.YcDataDTO;
import com.xywg.equipment.monitor.modular.whf.model.ProjectEnvironmentMonitorDataModel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description 扬尘解析器
 * Date: Created in 16:28 2018/10/15
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
public class YcDataHandler extends SimpleChannelInboundHandler<YcDataDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(YcDataHandler.class);


    public final static String SJJ_INFO_SUFF="device_platform:str:deviceinfo:lift";
    public final static String TJ_INFO_SUFF="device_platform:str:deviceinfo:crane";

    public static  final String type_monitor = "monitor";
    public static  final String type_crane = "crane";
    public static final String type_lift = "lift";

    @Autowired
    ZbusProducerHolder   zbusProducerHolder  ;
    @Autowired
    ServerProperties serverProperties;

    @Autowired
    XywgProerties xywgProerties;
    @Autowired
    RedisUtil redisUtil ;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, YcDataDTO msg) throws Exception {
        LOGGER.info("###########扬尘数据处理器接到数据############");
        String data = msg.getData();
        String sn = data.substring(6,17);
        zbusProducerHolder.sendMonitorTestMessage(sn,data);
        if(NettyChannelManage.isOn(sn,type_monitor,redisUtil,ctx,serverProperties.getName())){
            //上线
            String jsondata  = "{\"deviceNo\":\""+sn+"\",\"deviceTime\":"+msg.getNow().getTime()+"}";
            zbusProducerHolder.sendYcHtMessage(jsondata);
        }

        //模拟配置
        ProjectEnvironmentMonitorDataModel dataModel  = new ProjectEnvironmentMonitorDataModel();
        //设置 编号配对key
        dataModel.setDeviceNo("sdsyr");
        //设置 pm2.5配对字段
        dataModel.setPm25("a");
        //设置pm10 配对字段
        dataModel.setPm10("b");
        //设置温度 配对字段
        dataModel.setTemperature("c");
        //设置适度 配对字段
        dataModel.setHumidity("d");
        //设置噪音 配对字段
        dataModel.setNoise("e");
        //设置风速
        dataModel.setWindSpeed("j");
        //设置 风向
        dataModel.setWindDirection("k");
        //去除 :,  或  :结尾的数据
        data  = StrReplace.removeBlank(data);

        data =  StrReplace.replaceHead(data,dataModel.getDeviceNo(),"deviceNo");
        data =  StrReplace.replaceKey(data,dataModel.getPm25(),"pm25");
        data =  StrReplace.replaceKey(data,dataModel.getPm10(),"pm10");
        data =  StrReplace.replaceKey(data,dataModel.getTemperature(),"temperature");
        data =  StrReplace.replaceKey(data,dataModel.getHumidity(),"humidity");
        data =  StrReplace.replaceKey(data,dataModel.getNoise(),"noise");
        data =  StrReplace.replaceKey(data,dataModel.getWindSpeed(),"windSpeed");
        data =  StrReplace.replaceKey(data,dataModel.getWindDirection(),"windDirection");

        Long time  = System.currentTimeMillis();

        String jsonStr = "{"+data+",deviceTime:"+time+",createTime:"+time+"}";


        zbusProducerHolder.sendYcDataMessage(jsonStr);
        //判断是否需要转发
        if(redisUtil.get(xywgProerties.getRedisYcDispatchPrefix() + sn)!=null)
        {
            zbusProducerHolder.sendDispatchMessage(jsonStr);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
            LOGGER.info("###########有设备连接到扬尘数据处理器############");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                LOGGER.info("###60秒未收到客户端消息，主动关闭连接###");
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                /*写超时*/
                // System.out.println("===服务端===(WRITER_IDLE 写超时)");
            } else if (event.state() == IdleState.ALL_IDLE) {
                /*总超时*/
                // System.out.println("===服务端===(ALL_IDLE 总超时)");
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        Session session = ctx.channel().attr(NettyChannelManage.NETTY_CHANNEL_KEY).get();

        if(session!=null){
            String localKey= session.getType()+":"+session.getSn();
            //清除关系
            String redisDeviceKey =  "device_platform:devices:"+session.getType()+":"+session.getSn();
            String redisServerKey =  "device_platform:channel:"+serverProperties.getName()+"#"+ctx.channel().remoteAddress().toString();
            redisUtil.remove(redisDeviceKey,redisServerKey);
            NettyChannelManage.CHANNEL_MAP.remove(localKey);

            if(localKey.startsWith(type_monitor)){
                zbusProducerHolder.sendYcLxMessage(localKey);
            }else if(localKey.startsWith(type_lift)){
                zbusProducerHolder.sendSjjLxMessage(localKey);
            }else if(localKey.startsWith(type_crane)){
                zbusProducerHolder.sendTjLxMessage(localKey);
            }
        }
        LOGGER.info("###########有设备断开扬尘数据处理器############");
    }




}
