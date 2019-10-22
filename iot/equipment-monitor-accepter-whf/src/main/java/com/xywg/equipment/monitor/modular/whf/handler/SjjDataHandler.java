package com.xywg.equipment.monitor.modular.whf.handler;

import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.ServerProperties;
import com.xywg.equipment.monitor.core.util.NettyChannelManage;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.core.util.StrReplace;
import com.xywg.equipment.monitor.modular.whf.dto.SjjDataDTO;
import com.xywg.equipment.monitor.modular.whf.init.WhfChannelInit;
import com.xywg.equipment.monitor.modular.whf.model.ProjectLiftDataModel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:09 2018/10/23
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
public class SjjDataHandler extends SimpleChannelInboundHandler<SjjDataDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SjjDataHandler.class);
    @Autowired
    ZbusProducerHolder  zbusProducerHolder;
    @Autowired
    ServerProperties serverProperties;

    @Autowired
    RedisUtil redisUtil ;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SjjDataDTO msg) throws Exception {
        LOGGER.info("##############升降机数据处理器收到数据###################");
        String data = msg.getData();
        String sn = data.substring(8,19);
        zbusProducerHolder.sendLiftTestMessage(sn,data);
        if(NettyChannelManage.isOn(sn,YcDataHandler.type_lift,redisUtil,ctx,serverProperties.getName())){
            //上线
            String jsondata  = "{\"deviceNo\":\""+sn+"\",\"deviceTime\":"+msg.getNow().getTime()+"}";
            zbusProducerHolder.sendSjjHtMessage(jsondata);
        }

        ProjectLiftDataModel dataModel = new ProjectLiftDataModel();
        dataModel.setDeviceNo("dltysjj");
        dataModel.setWeight("a");
        dataModel.setHeight("b");
        dataModel.setSpeed("c");
        dataModel.setFrontDoorStatus("d");
        dataModel.setBackDoorStatus("e");
        dataModel.setStatus("f");
        dataModel.setKey1("g");
        dataModel.setKey2("h");
        dataModel.setKey3("i");
        dataModel.setKey4("j");
        dataModel.setKey5("k");
        dataModel.setKey6("l");
        dataModel.setKey7("m");
        //  去除  :, 和 :结尾的数据
        data = StrReplace.removeBlank(data);

        data = StrReplace.replaceHead(data, dataModel.getDeviceNo(), "deviceNo");
        data = StrReplace.replaceKey(data, dataModel.getWeight(), "weight");
        data = StrReplace.replaceKey(data, dataModel.getHeight(), "height");
        data = StrReplace.replaceKey(data, dataModel.getSpeed(), "speed");
        data = StrReplace.replaceKey(data, dataModel.getFrontDoorStatus(), "frontDoorStatus");
        data = StrReplace.replaceKey(data, dataModel.getBackDoorStatus(), "backDoorStatus");
        data = StrReplace.replaceKey(data, dataModel.getStatus(), "status");
        data = StrReplace.replaceKey(data, dataModel.getKey1(), "key1");
        data = StrReplace.replaceKey(data, dataModel.getKey2(), "key2");
        data = StrReplace.replaceKey(data, dataModel.getKey3(), "key3");
        data = StrReplace.replaceKey(data, dataModel.getKey4(), "key4");
        data = StrReplace.replaceKey(data, dataModel.getKey5(), "key5");
        data = StrReplace.replaceKey(data, dataModel.getKey6(), "key6");
        data = StrReplace.replaceKey(data, dataModel.getKey7(), "key7");

        Long time  = System.currentTimeMillis();

        String jsonStr = "{"+data+",deviceTime:"+time+",createTime:"+time+"}";


        zbusProducerHolder.sendSjjDataMessage(jsonStr);
        //判断是否需要转发
        if(redisUtil.get("xywg:sjj:dispatch:"+sn)!=null)
        {
            zbusProducerHolder.sendDispatchMessage(jsonStr);
        }

    }
}
