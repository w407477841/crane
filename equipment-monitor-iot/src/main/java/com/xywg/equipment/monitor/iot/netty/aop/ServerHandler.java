package com.xywg.equipment.monitor.iot.netty.aop;


import com.xywg.equipment.monitor.iot.core.util.RedisUtil;
import com.xywg.equipment.monitor.iot.modular.base.dto.MasterProtocolConfigDTO;
import com.xywg.equipment.monitor.iot.modular.base.handler.BaseDevice;
import com.xywg.equipment.monitor.iot.modular.base.model.ProjectErrorData;
import com.xywg.equipment.monitor.iot.modular.base.service.IMasterProtocolConfigService;
import com.xywg.equipment.monitor.iot.modular.base.service.IProjectErrorDataService;
import com.xywg.equipment.monitor.iot.modular.base.util.ApplicationContextProvider;
import com.xywg.equipment.monitor.iot.netty.common.enums.DeviceEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.zbus.kit.logging.Logger;
import io.zbus.kit.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * 数据接收
 * 
 */
@Component
@ChannelHandler.Sharable
@SuppressWarnings("all")
public class ServerHandler extends SimpleChannelInboundHandler<String> {
	
    private Logger logger = LoggerFactory.getLogger(ServerHandler.class);
    @Autowired
    ZbusProducerHolder  producerHolder;
    /** 协议头配置 */
    @Autowired
    IMasterProtocolConfigService protocolConfigService;
    @Autowired
    IProjectErrorDataService  projectErrorDataService;
    @Autowired
    private RedisUtil   redisUtil;

    public static Map<String,Channel> channels = new ConcurrentHashMap<>();

    private static final long CACHE_MINS = 60*24;

    /**
     * @param ctx     通道处理的上下文信息
     * @param message 接收的消息
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String  message) {

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
                .remoteAddress();

        	//1 . 获取所有协议头
        	//2 . 有没有符合的协议
            //3   获得该协议的设备类别
            //4   使用 处理类处理
        
        String msg = message;
        ProjectErrorData    projectErrorData  =new ProjectErrorData();
        projectErrorData.setCreateTime(new Date());
        projectErrorData.setData(msg+"   "+insocket.getAddress()+":"+insocket.getPort());
        projectErrorDataService.insert(projectErrorData);
        //匹配 心跳数据

        

        // 会抛异常
        MasterProtocolConfigDTO config = null ;
        try {
             config= protocolConfigService.protocolConfig(msg);

            Class<? extends BaseDevice> clazz = DeviceEnum.getDeviceClass(config.getKey());

            if(clazz == null){
                System.out.println( "["+msg+"]未匹配到 配置信息");
                return ;
            }
            //
            if(msg.startsWith("ycht")||msg.startsWith("sjjht")||msg.startsWith("qzjht")){
                //redis key
                String redisKey = "device_platform:oldchannel:"+"#"+ctx.channel().remoteAddress().toString();
                //不存在 就添加
                if(!redisUtil.exists(redisKey)){
                    channels.put(msg,ctx.channel());
                    redisUtil.set(redisKey,msg,CACHE_MINS);
                }
            }
            BaseDevice   device =  ApplicationContextProvider.getBean(clazz);
            device.handler(msg,producerHolder,config);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }







    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("##############设备主动退出连接#############");
        String redisKey = "device_platform:oldchannel:"+"#"+ctx.channel().remoteAddress().toString();
        if(redisUtil.exists(redisKey)){
            String localKey = (String) redisUtil.get(redisKey);
            Class<? extends BaseDevice> clazz = null;
            if(localKey.startsWith("ycht")){
                 clazz = DeviceEnum.sdsyr.getDevice();
            }else if(localKey.startsWith("sjjht")){
                clazz = DeviceEnum.dltysjj.getDevice();
            }else if(localKey.startsWith("tjht")){
                clazz = DeviceEnum.dltytj.getDevice();
            }
            if(clazz!=null){
                BaseDevice   device =  ApplicationContextProvider.getBean(clazz);
                channels.remove(localKey);
                device.offline(localKey);
            }
        }
    }

    //    private String getMessage(ByteBuf buf) {
//
//
//    	byte[] con = new byte[buf.readableBytes()];
//    	buf.readBytes(con);
//    	try {
//    	return new String(con,"UTF-8");
//    	} catch (UnsupportedEncodingException e) {
//    	e.printStackTrace();
//    	return null;
//    	}
//    	}

    
    /**
     * 回复消息给客户端
     *
     * @param ctx     上下文
     * @param message responseMessage
     */
    public void responseMessage(ChannelHandlerContext ctx, String message) {
        ByteBuf resp = Unpooled.copiedBuffer(message.getBytes());
        ctx.writeAndFlush(resp);
    }

    /**
     * 这个方法会在发生异常时触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = insocket.getAddress().getHostAddress();
        this.logger.error(clientIP + " on " + new Date().toString() + ":ExceptionOccurred");
        cause.printStackTrace();
        ctx.close();
    }
/*
    public String selectDataType(String msg) throws Exception {
        this.logger.debug("<< " + msg);
        //根据消息获取枚举值
        CommandToken token = CommandToken.parse(msg);

        String out = null;
        //根据报文类型选择处理:
        switch (token) {
            //在考勤机修改用户的时候,先发请求信息
            case PostEmployee:
                out = handlePostEmployee(msg);
                break;
            //在考勤机修改用户的时候,发送具体信息
            case Employee:
                out = handleEmployee(msg);
                break;
            //在考勤机考勤用户的时候,先发请求信息
            case PostRecord:
                out = handlePostRecord(msg);
                break;
            //在考勤机考勤用户的时候,先发具体信息(考勤对象的基本信息)
            case Record:
                // 考勤2.
                out = handleRecord(msg);
                break;
            //根据设备id返回命令文本(向考勤机发送命令)
            case GetRequest:
                out = handleGetRequest(msg);
                break;
            //考勤机如返回"quit时则断开连接"
            case Return:
                out = handleReturn(msg);
                break;
            case SysUpgrade:
                handleSysUpgrade(msg);
                break;
            default:
                return null;
        }
        this.logger.debug(">> " + out);
        return out;
    }

*/
    /*
    private String handlePostEmployee(String msg) {
        getDeviceInfo(msg);
        if (device != null) {
            return "Return(result=\"success\")";
        }
        return null;
    }

    private String handleEmployee(String msg) {
        if (device == null) {
            this.logger.error("设备信息丢失2");
            return null;
        }
        return collectDataHandleService.saveEmployee(msg, device);
    }

    private String handlePostRecord(String msg) {
        getDeviceInfo(msg);
        if (device != null) {
            return "Return(result=\"success\" postphoto=\"true\")";
        }
        return null;
    }

    private void handleSysUpgrade(String msg) {
        if (command != null) {
            collectDataHandleService.processUpgradeFailed(msg,command);
        }
    }*/

    /**
     * 设备执行命令结果
    
    private String handleReturn(String msg) {
        if (device == null) {
            command = null;
            this.logger.error("设备信息丢失1");
            return "Quit()";
        }
        if (command == null) {
            logger.error(device.getId() + "：命令丢失");
            return "Quit()";
        }
        try {
            //如果命令执行成功
            if (Utils.hasSuccessValue(msg)) {
                command.setState(1L);
            } else {
                //如果失败
                command.setState(-1L);
                command.setDescription(Utils.getValue(msg, "reason"));
            }
            //更新命令执行状态
            deviceCommandMapper.setCommandState(command);
            //如果这条指令是获取设备信息
            if (command.getType() == 3) {
                collectDataHandleService.saveDeviceInfo(msg, device);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            command = null;
        }
        return "Quit()";
    }

 */
    /**
     * 设备请求指令
    
    private String handleGetRequest(String msg) {
        getDeviceInfo(msg);
        if (device == null) {
            return "Quit()";
        }
        if (command != null) {
            return "CheckUpgradeStatus()";
        }
        //得到最早的一条未执行的命令集
        command = deviceCommandMapper.getDeviceCommand(device.getSn());
        if (command == null) {
            return "Quit()";
        }
        return command.toString();
    }
 */
    /**
     * 真正的考勤数据处理
   
    private String handleRecord(String msg) {
        if (device == null) {
            this.logger.error("设备信息丢失3");
            return null;
        }

        String userId = Utils.getValue(msg, "id");
        if (Utils.isEmpty(userId)) {
            this.logger.error("人员ID缺失");
            return "Return(result=\"failed\")";
        }

        String time = Utils.getValue(msg, "time");
        if (Utils.isEmpty(time)) {
            this.logger.error("考勤时间缺失");
            return "Return(result=\"failed\")";
        }
        String photo = Utils.getValue(msg, "photo");
        DeviceRecord record = new DeviceRecord();
        //小写的mm表示的是分钟
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            record.setTime(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (record.getTime() == null) {
            this.logger.error("解析考勤时间失败");
            return "Return(result=\"failed\")";
        }

        byte[] blob = Utils.toBlob(photo);
        if ((blob != null) && (blob.length > 0)) {
            record.setPhoto(Utils.saveFile("records", ".jpg", blob, record.getTime()));
        }
        WorkerMaster personnel = new WorkerMaster();
        personnel.setWorkerName(Utils.getValue(msg, "name"));
        personnel.setIdCardNumber(userId);
        record.setPerson(personnel);
        record.setDevice(device);

        //保持数据到数据库
        try {
            collectDataHandleService.saveRecord(record);
        } catch (Exception e) {
            e.printStackTrace();
            return "Return(result=\"failed\")";
        }
        return "Return(result=\"success\")";
    }
  */

    /**
     * 获取设备具体信息
  
    private void getDeviceInfo(String msg) {
        String sn = Utils.getValue(msg, "sn");
        if (StringUtils.isBlank(sn)) {
            logger.error("设备序列化丢失");
            return;
        }

        Device dbDevice = deviceService.deviceHeartbeatArrived(sn);
        if (dbDevice != null) {
            //dbDevice.setSn(sn);
            //本次连接的设备
            device = dbDevice;
        } else {
            logger.error(sn + ":设备不在系统中");
        }
    }
       */
    
}

