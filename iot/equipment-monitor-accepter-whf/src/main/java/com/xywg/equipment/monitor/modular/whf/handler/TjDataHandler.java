package com.xywg.equipment.monitor.modular.whf.handler;

import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.ServerProperties;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.NettyChannelManage;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.core.util.StrReplace;
import com.xywg.equipment.monitor.modular.whf.dto.TjDataDTO;
import com.xywg.equipment.monitor.modular.whf.init.WhfChannelInit;
import com.xywg.equipment.monitor.modular.whf.model.ProjectCraneDataModel;
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
 * Date: Created in 14:22 2018/10/23
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
public class TjDataHandler extends SimpleChannelInboundHandler<TjDataDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TjDataHandler.class);

    @Autowired
    ZbusProducerHolder zbusProducerHolder  ;

    @Autowired
    ServerProperties serverProperties;

    @Autowired
    RedisUtil redisUtil ;

    @Autowired
    private XywgProerties xywgProerties;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TjDataDTO msg) throws Exception {
        LOGGER.info("###########塔基数据处理器接收到数据#############");
        String data = msg.getData();
        String sn = data.substring(7,18);
        zbusProducerHolder.sendCraneTestMessage(sn,data);
        // 本地map key
        if(NettyChannelManage.isOn(sn,YcDataHandler.type_crane,redisUtil,ctx,serverProperties.getName())){
            //上线
            String jsondata  = "{\"deviceNo\":\""+sn+"\",\"deviceTime\":"+msg.getNow().getTime()+"}";
            zbusProducerHolder.sendTjHtMessage(jsondata);
        }
        //TODO 先模拟
        ProjectCraneDataModel dataModel  =new ProjectCraneDataModel();
        dataModel.setDeviceNo("dltytj");
        dataModel.setWeight("a");
        dataModel.setRange("b");
        dataModel.setHeight("c");
        dataModel.setRotaryAngle("d");
        dataModel.setMoment("e");
        dataModel.setMomentPercentage("f");
        dataModel.setWindSpeed("g");
        dataModel.setTiltAngle("h");

        data = StrReplace.removeBlank(data);

        //以上为 模拟 ，后期从配置表中取

        data =  StrReplace.replaceHead(data,  dataModel.getDeviceNo(),"deviceNo");
        data =  StrReplace.replaceKey(data, dataModel.getWeight(), "weight");
        data =  StrReplace.replaceKey(data, dataModel.getRange(), "range");
        data =  StrReplace.replaceKey(data, dataModel.getHeight(), "height");
        data =  StrReplace.replaceKey(data, dataModel.getRotaryAngle(), "rotaryAngle");
        data =  StrReplace. replaceKey(data, dataModel.getMoment(), "moment");
        data =  StrReplace.replaceKey(data, dataModel.getMomentPercentage(), "momentPercentage");
        data =  StrReplace.replaceKey(data, dataModel.getWindSpeed(), "windSpeed");
        data =  StrReplace.replaceKey(data, dataModel.getTiltAngle(), "tiltAngle");
        // 报警字段
        data =  StrReplace.replaceKey(data, "i","status");

        Long time  = System.currentTimeMillis();

        String jsonStr = "{"+data+",deviceTime:"+time+",createTime:"+time+"}";

        zbusProducerHolder.sendTjDataMessage(jsonStr);

        zbusProducerHolder.sendDispatchMessage(jsonStr);
        if(redisUtil.get(xywgProerties.getRedisTdDispatchPrefix()+sn)!=null)
        {
            zbusProducerHolder.sendDispatchMessage(jsonStr);
        }

    }
}
