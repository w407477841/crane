package com.xywg.equipment.sandplay.modular.sandplay.controller;

import com.xywg.equipment.sandplay.config.properties.BaseCmd;
import com.xywg.equipment.sandplay.core.enums.DeviceType;
import com.xywg.equipment.sandplay.core.util.ApplicationContextProvider;
import com.xywg.equipment.sandplay.core.util.WeightThread;
import com.xywg.equipment.sandplay.core.util.rxtx.RxtxExport;
import com.xywg.equipment.sandplay.core.util.rxtx.SerialTool;
import com.xywg.equipment.sandplay.nettty.twoinone.AllChannelInit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


import java.util.*;

import static com.xywg.equipment.sandplay.nettty.twoinone.AllChannelInit.KEY;
import static com.xywg.equipment.sandplay.nettty.twoinone.AllChannelInit.channelMap;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:43 2018/9/25
 * Modified By : wangyifei
 */
@RestController
@CrossOrigin
@RequestMapping("sandplay")
public class SandplayController extends BaseController{

    private static final Logger  logger = LoggerFactory.getLogger(SandplayController.class);




    @GetMapping(value = "getStatus")
    public Object getStatus(){
        if(AllChannelInit.channelMap.get(AllChannelInit.KEY) ==null){
            return "no";
        }
        return "ok";
    }


/**
* @author: wangyifei
* Description: 返回所有类别 ,及数量
 *
* Date: 9:13 2018/9/26
*/
    @GetMapping(value = "getAllType")
    public Object getAllType(){

        List<Map<String,Object>>  allType =  new ArrayList<>();
        for(DeviceType type : DeviceType.values()){
            Map<String,Object> map = new HashMap<>(2);
            map.put("name",type.getName());
            map.put("type",type.getType());

            BaseCmd deviceCmd =  ApplicationContextProvider.getBean(type.getClazz());
            List<String> addr  =  deviceCmd.getAddr();
               if(addr!=null && addr.size()>0){
                   map.put("quantity",addr.size());
               }else{
                   map.put("quantity",0);
               }

            allType.add(map);
        }
        return allType;
    }


/**
* @author: wangyifei
* Description:
* Date: 8:55 2018/9/26
 * @param index  设备下标
 * @param type   设备类型
 * @param  sendType 发送类型   0 TCP; 1 串口
*/
    @GetMapping("action")
    public Object doAction(@RequestParam(value="index",required = true) Integer index,@RequestParam(value="type",required = true)  String type  , @RequestParam(value="sendType",required = true) Integer sendType  ){
    boolean  success =  true;
         // 发送数据    头1+地址1+控制码1+数据4+校验1
        String   sendData =  getSendData(type,index);
        logger.info("#################################");
        logger.info(" 发送数据 " + sendData);
        logger.info("#################################");
        if(0==sendType){
            Channel channel =  channelMap.get(KEY);
            if(channel == null){
                logger.info("################################");
                logger.info(" 沙盘未连接");
                logger.info("################################");
                success = false;
            }
            try {
                if (!channel.isActive()) {
                    logger.info("################################");
                    logger.info(" 沙盘已离线");
                    logger.info("################################");
                    channel.close();
                    success = false;
                } else {

                    ByteBuf byteBuf = Unpooled.copiedBuffer(("01"+sendData).getBytes());
                    channel.writeAndFlush(byteBuf);
                    if(DeviceType.WEIGHT.getType().equals(type)){
                        WeightThread   weightThread    = new WeightThread();
                        new Thread(weightThread).start();
                    }
                }
            } catch (Exception ex) {
                success = false;
            }
    }else if(1 == sendType){
            RxtxExport re = new RxtxExport();
            try{
                re.sendMsg(SerialTool.comm,sendData);

            }catch(Exception e){
                success = false;
            }
    }else{
             success = false;
    }

        return success?"ok":"no";
    }


    @GetMapping("allOpen")
    public Object allOpen(@RequestParam(value="sendType",required = true) Integer sendType){
        boolean  success  =true;

        String open = cmd.getAllOpen();
        String close = cmd.getAllClose();
        logger.info("#################################");
        logger.info(" 发送数据  全开" + open);
        logger.info(" 发送数据  全关" + close);
        logger.info("#################################");
        if(sendType == 0){
                Channel channel =  channelMap.get(KEY);
                if(channel == null){
                    logger.info("#################################");
                    logger.info(" 沙盘未连接");
                    logger.info("#################################");
                    success = false;
                }
                try {
                    if (!channel.isActive()) {
                        logger.info("#################################");
                        logger.info(" 沙盘已离线");
                        logger.info("#################################");
                        channel.close();
                        success = false;
                    } else {
                        ByteBuf byteBuf = Unpooled.copiedBuffer(("01"+open).getBytes());
                        channel.writeAndFlush(byteBuf);
                            WeightThread   weightThread    = new WeightThread();
                            new Thread(weightThread).start();
                            Thread.sleep(1000);
                            byteBuf = Unpooled.copiedBuffer(("01"+close).getBytes());
                        channel.writeAndFlush(byteBuf);
                    }
                } catch (Exception ex) {
                    success = false;
                }

        }else if(sendType  ==1){
            RxtxExport re = new RxtxExport();
             re.sendMsg(SerialTool.comm,open);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            re.sendMsg(SerialTool.comm,close);
        }else{
            success = false;
        }

        return success?"ok":"no";
    }




}
