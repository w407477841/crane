package com.xywg.equipment.monitor.iot.config.rabbitmq.consumer.impl;

import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import com.xingyun.crane.cache.RedisUtil;
import com.xywg.equipment.monitor.iot.config.rabbitmq.consumer.BaseConsumer;
import com.xywg.equipment.monitor.iot.netty.device.dto.ResponseDTO;
import com.xywg.equipment.monitor.iot.netty.device.dto.WSAlarmDTO;
import com.xywg.equipment.monitor.iot.netty.device.dto.WSDetailDTO;
import com.xywg.equipment.monitor.iot.netty.device.handler.CommonMethod;
import com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod;
import com.xywg.equipment.monitor.iot.netty.device.handler.NettyChannelManage;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:55 2019/7/15
 * Modified By : wangyifei
 */
@Component
@RabbitListener(queues = {"queue.response"})
@Slf4j
public class MqConsumer  extends BaseConsumer {

    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void handler0(String hello, Channel channel, Message message) {
        ResponseDTO responseDTO = JSONUtil.toBean(hello,ResponseDTO.class);
            io.netty.channel.Channel nettyChannel =  NettyChannelManage.CHANNEL_MAP.get(responseDTO.getSn());

                if(responseDTO.getCmd().equals("0001"))
                {
                    if(nettyChannel!=null){
                        nettyChannel.writeAndFlush(Unpooled.copiedBuffer(CommonStaticMethod.toBytes(responseDTO.getHexMessage())));
                    }

                }
                //下线原因返回消息
                  if(responseDTO.getCmd().equals("0014")){
                      // 清除登录信息
                  }
                if(responseDTO.getCmd().equals("0018")){
                    Map<String,Object> responseMap=JSONUtil.toBean(responseDTO.getHexMessage(), HashedMap.class);
                    log.info("塔吊--设备{} 下线提醒",responseDTO.getSn());
                    //下线提醒
                    commonMethod.pushOffline((String)responseMap.get("uuid"), responseMap.get("id") + "", responseDTO.getSn());
                }

                if(responseDTO.getCmd().equals("0020")){
                    WSDetailDTO detailDTO = JSONUtil.toBean(responseDTO.getHexMessage(),WSDetailDTO.class);
                    commonMethod.push(redisUtil, detailDTO.getSn(),detailDTO.getType(),detailDTO.getUuid(), detailDTO.getCurrentCraneData(), detailDTO.getProjectId());
                }
        if(responseDTO.getCmd().equals("0021")){
            WSAlarmDTO detailDTO = JSONUtil.toBean(responseDTO.getHexMessage(),WSAlarmDTO.class);
            commonMethod.push(redisUtil, detailDTO.getSn(),detailDTO.getType(),detailDTO.getUuid(), detailDTO.getResultDTO(), detailDTO.getProjectId());
        }
    }
}
