package com.xywg.equipment.monitor.modular.whf.handler;

import cn.hutool.core.util.StrUtil;
import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.ServerProperties;
import com.xywg.equipment.monitor.core.netty.Session;
import com.xywg.equipment.monitor.core.util.NettyChannelManage;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.dto.DbDataDTO;
import com.xywg.equipment.monitor.modular.whf.init.DbChannelInit;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;



/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:09 2018/10/23
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
public class DbHandler extends SimpleChannelInboundHandler<DbDataDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbHandler.class);

    private final String type_meter = "meter";

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ServerProperties serverProperties;
    /** 0x33
     *
     * 接收需要 减去
     * 发送需要 加上
     * */
    public static final int CHANGE_NUM = 51;

    @Autowired
    ZbusProducerHolder  zbusProducerHolder;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DbDataDTO msg) throws Exception {
        LOGGER.info("##############数据处理器数据###################");
        String data = msg.getData();
        String ori =  data ;
        String addr = msg.getAddr();
        LOGGER.info("##############原始数据："+data+"###################");
        LOGGER.info("##############原始地址："+addr+"###################");
        //转换 数据   收到 AB 89 67 45 实际为 12 34 56 . 78

        data = reverseData(data);
        addr = reverseAddr(addr);
        LOGGER.info("##############转换后的数据："+data+"###################");
        LOGGER.info("##############转换后的地址："+addr+"###################");
        String send = DbChannelInit.sendMessage.get(addr);
        Long time  = System.currentTimeMillis();

        String jsonStr = "{data:"+data+",ori:"+ori+",send:"+send+",deviceNo:"+addr+",deviceTime:"+time+",createTime:"+time+"}";


        zbusProducerHolder.sendDbDataMessage(jsonStr);



    }

    /**
     *  转换 数据
     *    收到 AB 89 67 45
     *    实际为 12 34 56 . 78
     * @param data
     * @return
     */
     private String reverseData (String data){
         String []  d = new String [4] ;
         d[0] = reduceChangeNum(data.substring(6,8));
         d[1] = reduceChangeNum(data.substring(4,6));
         d[2] = reduceChangeNum(data.substring(2,4));
         d[3] = reduceChangeNum(data.substring(0,2));
         data = d[0]+d[1]+d[2]+"."+d[3];
         return data;
     }

    /**
     * 将地址为反转
     *
     *  原始 030002061820
     * 转换后 201806020003
     * @param addr
     * @return
     */
    private String reverseAddr(String addr){
        String now = new String();
        for(int i = 6;i>0;i--){
            now += StrUtil.sub(addr,(i-1)*2,i*2);
        }
        return now;

    }

    /**
     *  减去 51
     * @param ori
     * @return
     */
    private String reduceChangeNum(String ori){
        //原始值
        int ori_i =   Integer.parseInt(ori,16);
        String now =  Integer.toHexString(ori_i-CHANGE_NUM);
        if(now.length()<2){
            return "0"+now;
        }
        return now;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOGGER.info("###########有设备连接到电表表数据处理器############");
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
        if(session!=null) {
            List<String> list = session.getServerNo();
            for(int i = 0;i < list.size();i++) {
                String localKey = session.getType() + ":" + list.get(i);
                //清除关系
                String redisDeviceKey = "device_platform:devices:" + session.getType() + ":" + list.get(i);
                String redisServerKey = "device_platform:channel:" + serverProperties.getName() + "#" + ctx.channel().remoteAddress().toString();
                redisUtil.remove(redisDeviceKey, redisServerKey);
                NettyChannelManage.CHANNEL_MAP.remove(localKey);
                DbChannelInit.channels.remove(list.get(i));

                if (localKey.startsWith(type_meter)) {
                    zbusProducerHolder.sendDbLxMessage(localKey);
                }
            }



        }
        LOGGER.info("###########有设备断开电表数据处理器############");
        /*LOGGER.info("###执行关闭逻辑###");
        //删除本地缓存
        Map<String,Channel>  channels = DbChannelInit.channels;
        for(String sn:channels.keySet()){
            if(channels.get(sn).remoteAddress().toString() .equals(ctx.channel().remoteAddress().toString())){
                // 可以做设备离线操作
                zbusProducerHolder.sendDbLxMessage(sn);
                DbChannelInit.channels.remove(sn);
            }


        }*/



    }
}
