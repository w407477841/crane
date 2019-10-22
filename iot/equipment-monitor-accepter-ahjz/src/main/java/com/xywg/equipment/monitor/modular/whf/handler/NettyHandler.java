package com.xywg.equipment.monitor.modular.whf.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.DataUtil;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.dto.TdDTO;
import com.xywg.equipment.monitor.modular.whf.dto.UserInfoDTO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:17 2019/1/7
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
@SuppressWarnings("all")
public class NettyHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyHandler.class);

    public static AtomicInteger  count = new AtomicInteger();
    private static final String HEADER =  "A55A";
    private static final String FOOTER =  "CC33C33C";

    @Autowired
    ZbusProducerHolder zbusProducerHolder  ;

    @Autowired
    private XywgProerties xywgProerties;

    /**
     *  绑定通道
     */
    private static final AttributeKey<XySession> NETTY_CHANNEL_KEY = AttributeKey.valueOf("netty.channel");

    /**
     * 本地通道缓存
     */
    public static Map<String,Channel> nettyChannels = new ConcurrentHashMap<>(10);
    @Autowired
    private RedisUtil redisUtil ;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        //可读长度
        int readableLength = buf.readableBytes();
        byte[] msgData = new byte[readableLength];
        buf.readBytes(msgData);
        String originalData = DataUtil.bytesToHexString(msgData).toUpperCase();
        originalData = originalData.substring( originalData.indexOf(HEADER));
        System.out.println(originalData);
        //数据合法性检查
        if(!valid(originalData)){
            throw new RuntimeException("非法数据 <"+originalData+">");
        }
        sendTest(originalData);
        //类型
        String type = this.type(originalData);
        switch (type){
            // 注册
            case "60" :
            this.zhuce(originalData,ctx);
            break;
            // 人员身份认证信息
            case "12" :
            this.renyuanxinxi(originalData,ctx);
            break;
            //基本信息
            case "31" :
            this.jibenxinxi(originalData,ctx);
            break;
            case "20":
            this.baojingshuju(originalData,ctx);
            break;
            //传感器
            case "33" :
            break;
            // 报警设置信息
            case "35" :
            break;
            // 限位设置信息
            case "37" :
            break;
            // 障碍物信息
            case "2F" :
            break;
            case "0C" :
            this.shishishuju(originalData,ctx);
            break;
            //心跳
            case "80" :
                this.xintiao(originalData,ctx);
                break;


        }


    }

    private String time(String hex){
        StringBuffer sb =new StringBuffer();
        sb.append(2000+Integer.parseInt(StrUtil.sub(hex,0,2),16))
                .append(StrUtil.fillBefore(Integer.parseInt(StrUtil.sub(hex,2,4),16)+"",'0',2))
                .append(StrUtil.fillBefore(Integer.parseInt(StrUtil.sub(hex,4,6),16)+"",'0',2))
                .append(StrUtil.fillBefore(Integer.parseInt(StrUtil.sub(hex,6,8),16)+"",'0',2))
                .append(StrUtil.fillBefore(Integer.parseInt(StrUtil.sub(hex,8,10),16)+"",'0',2))
                .append(StrUtil.fillBefore(Integer.parseInt(StrUtil.sub(hex,10,12),16)+"",'0',2));
        return sb.toString();
    }

    /**
     *  登录
     */
    private void login(String data,ChannelHandlerContext ctx){
        if(!isLogin(ctx)) {
            String number = this.number(data);
            String sn = this.sn(data);
            String cj = this.content(data);
            String checkCode = this.checkCode(data);
            //校验
            this.equalsIgnoreCaseCheckCode(checkCode, data);
            XySession xySession = new XySession();
            xySession.setSn(sn);
            xySession.setType("crane");
            xySession.setCj(cj);
            xySession.setNumber(number);
            xySession.setId(RandomUtil.simpleUUID());

            //双向绑定
            ctx.channel().attr(NETTY_CHANNEL_KEY).set(xySession);
            nettyChannels.put(localKey("crane", sn(number, sn, cj)), ctx.channel());


            String jsondata = "{\"deviceNo\":\"" + sn(number, sn, cj) + "\",\"deviceTime\":" + System.currentTimeMillis() + "}";
            try {
                zbusProducerHolder.sendTjHtMessage(jsondata);
            } catch (Exception e) {

            }
        }
    }

    private void xintiao(String data,ChannelHandlerContext ctx){
        if(!isLogin(ctx)){
            throw  new RuntimeException("未登录");
        }
        String number = this.number(data);
        String sn = this.sn(data);
        String ret = HEADER + "8E" + number + sn + "3C" ;
        String retChechCode = doCheckCode(DataUtil.hexStringToBytes(ret));
        String length = length(ret);

        ret = ret + retChechCode + length + FOOTER;
        System.out.println("心跳返回:"+ret);
        ctx.writeAndFlush( Unpooled.copiedBuffer(DataUtil.hexStringToBytes(ret)));



    }

    /**
     *  注册
     * @param data
     * @param ctx
     */
    private void zhuce (String data,ChannelHandlerContext ctx){
            //A55A 60 01 000001  C0 10 CC33C33C
        login(data,ctx);
        // 返回 数据
        XySession xySession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        String number = xySession.getNumber();
        String sn = xySession.getSn();
        String cj = xySession.getCj();
        String ret = HEADER + "61" + number + sn + "01" + this.time() + "0F";
        String retChechCode = doCheckCode(DataUtil.hexStringToBytes(ret));
        String length = length(ret);
        ret = ret + retChechCode + length + FOOTER;
        ctx.writeAndFlush( Unpooled.copiedBuffer(DataUtil.hexStringToBytes(ret)));
        System.out.println("登录成功 <{"+sn(number,sn,cj)+"}>");
    }

    /**
     *  数据长度
     * @param data
     * @return
     */
    public static  String length(String data ){
        int  s= data.length()/2;
        String length = Integer.toHexString(s);
        length =   StrUtil.fillBefore(length,'0',2);
        return length;
    }
    /**
     * 基本信息
     * @param data
     * @param ctx
     */
 private void    jibenxinxi(String data,ChannelHandlerContext ctx){
     if(!isLogin(ctx)){
         throw  new RuntimeException("未登录");
     }

     //获取内容
     String content  =this.content(data);
     //解析
     TdDTO tdDTO =new TdDTO();
     tdDTO.setZuobiaox(toBigDecimal(Integer.parseInt(StrUtil.sub(content,0,4),16),0.1));
     tdDTO.setZuobiaoy(toBigDecimal(Integer.parseInt(StrUtil.sub(content,4,8),16),0.1));
     tdDTO.setQianbi(toBigDecimal(Integer.parseInt(StrUtil.sub(content,8,12),16),0.1));
     tdDTO.setHoubi(toBigDecimal(Integer.parseInt(StrUtil.sub(content,12,16),16),0.1));
     tdDTO.setTamaogao(toBigDecimal(Integer.parseInt(StrUtil.sub(content,16,20),16),0.1));
     tdDTO.setTabigao(toBigDecimal(Integer.parseInt(StrUtil.sub(content,20,24),16),0.1));
     tdDTO.setZuidadiaozhong(toBigDecimal(Integer.parseInt(StrUtil.sub(content,24,28),16),0.001));
     tdDTO.setZuidaliju(toBigDecimal(Integer.parseInt(StrUtil.sub(content,28,32),16),0.01));
     tdDTO.setTajixinghao(new String(DataUtil.hexStringToBytes(StrUtil.sub(content,32,62))));
     tdDTO.setChangjia(new String(DataUtil.hexStringToBytes(StrUtil.sub(content,62,92))));
     tdDTO.setJiaojiechangdu(toBigDecimal(Integer.parseInt(StrUtil.sub(content,92,96),16),0.1));
     //缓存到本地
      XySession xySession =  ctx.channel().attr(NETTY_CHANNEL_KEY).get();
      xySession.setProperties(JSONUtil.toJsonStr(tdDTO));
      System.out.println(xySession.getProperties());
      ctx.channel().attr(NETTY_CHANNEL_KEY).set(xySession);
 }

    private void shishishuju(String data,ChannelHandlerContext ctx){
        if(!isLogin(ctx)){
            throw  new RuntimeException("未登录");
        }
        //获取内容
        String content  =this.content(data);
        //跳过版本号 0-2
        //采集时间 2-14
        Date  collectTime = new Date();
        //跳过厂家及设备 14-16
            XySession xySession =ctx.channel().attr(NETTY_CHANNEL_KEY).get();
            String number = xySession.getNumber();
            String sn = xySession.getSn();
            String cj  =xySession.getCj();
            TdDTO tdDTO = JSONUtil.toBean(xySession.getProperties(),TdDTO.class);
             StringBuffer sb = new StringBuffer("{")
                //采集时间
                .append("deviceNo:").append(sn(number,sn,cj)).append(",")
                .append("deviceTime:").append(collectTime.getTime()).append(",")
                .append("createTime:").append(collectTime.getTime()).append(",")
                //高度 16-20
                .append("height:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,16,20),16),0.1).toString()).append(",")
                .append("range:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,20,24),16),0.1).toString()).append(",")
                .append("rotaryAngle:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,24,28),16),0.1).toString()).append(",")
                //跳过 起重量 28-32
                //风速
                .append("windSpeed:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,32,36),16),0.1).toString()).append(",")
                //倾角
                .append("tiltAngle:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,36,40),16),0.01).subtract(new BigDecimal(15)).toString()).append(",")
                //重量 根据重量百分比计算
                .append("weight:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,40,42),16),0.01).multiply(tdDTO.getZuidadiaozhong()).setScale(3,BigDecimal.ROUND_HALF_UP).toString()).append(",")
                //力矩百分比
                .append("momentPercentage:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,42,44),16),1.0).toString()).append(",")
                //力矩 根据百分比计算
                .append("moment:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,42,44),16),0.01).multiply(tdDTO.getZuidaliju()).setScale(3,BigDecimal.ROUND_HALF_UP).toString()).append(",")
                // 跳过 分速百分比 44-46
                // 跳过 倾斜百分比 46-48
                 .append(userinfo(ctx))
                .append(alarm(StrUtil.sub(content,48,50)))
                .append("}");

        try {
            zbusProducerHolder.sendTjDataMessage(sb.toString());
            //   依据缓存判断是否需要转发
            String deviceNo=sn(number,sn,cj);
            System.out.println("设备号是:"+deviceNo);
            System.out.println("缓存值是:"+redisUtil.get(xywgProerties.getRedisTdDispatchPrefix()+deviceNo));
            if(redisUtil.get(xywgProerties.getRedisTdDispatchPrefix()+deviceNo)!=null)
            {
                zbusProducerHolder.sendDispatchMessage(sb.toString());
            }
            LOGGER.info(sb.toString());
        } catch (Exception e) {
        }

    }

    /**
     * 组装人员信息
     * @param ctx
     * @return
     */
    private String userinfo(ChannelHandlerContext ctx){
        //获取当前通道session
        StringBuffer stringBuffer = new StringBuffer("");
        XySession currSession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        String   key = "device_platform:userinfo:crane:"+ sn(currSession.getNumber(),currSession.getSn(),currSession.getCj());
        if(redisUtil.exists(key)){
            String json = (String) redisUtil.get(key);
            UserInfoDTO userInfoDTO = JSONUtil.toBean(json,UserInfoDTO.class);
            if(userInfoDTO.getUsername()!=null&&userInfoDTO.getUsername().trim().length()>0){
                stringBuffer.append("key1:").append(userInfoDTO.getUsername().trim()).append(",");
            }
            if(userInfoDTO.getUserId()!=null&&userInfoDTO.getUserId().trim().length()>0){
                stringBuffer.append("key2:").append(userInfoDTO.getUserId().trim()).append(",");
            }
            if(userInfoDTO.getIdCard()!=null&&userInfoDTO.getIdCard().trim().length()>0){
                stringBuffer.append("key3:").append(userInfoDTO.getIdCard().trim()).append(",");
            }

        }
        return stringBuffer.toString();
    }

    /**
     * 处理人员信息
     * @param data
     * @param ctx
     */
    private void renyuanxinxi(String data,ChannelHandlerContext ctx){
//        if(!isLogin(ctx)){
//            throw  new RuntimeException("未登录");
//        }
        //获取内容
        String content  =this.content(data);
        // 0-2 识别结果状态值
        // 2-66 用户名
        // 66-82 用户id
        // 82-84 百分比
        // 84-118 身份证
        String status  =  StrUtil.sub(content,0,2);
        String number = this.number(data);
        String sn = this.sn(data);
        String cj = this.content(data);
        String deviceno = sn(number,sn,cj);
        if("00".equals(status)){
            // 指纹
          LOGGER.info("指纹{}",deviceno);

        }else {
            // 人脸认证
           LOGGER.info("人脸{}",deviceno);
        }
            String usernameHex =   StrUtil.sub(content,2,66);
            String username = HexUtil.decodeHexStr(usernameHex,CharsetUtil.CHARSET_GBK);
            String useridHex =  StrUtil.sub(content,66,82);
            String userid = HexUtil.decodeHexStr(useridHex);
            String accuracyHex = StrUtil.sub(content,82,84);
            Integer accuracy  = Integer.parseInt(accuracyHex,16);
            String idcardHex = StrUtil.sub(content,84,118);
            String idcard = HexUtil.decodeHexStr(idcardHex);

            UserInfoDTO userInfoDTO = new UserInfoDTO(username,userid,idcard,accuracy);


            String   key = "device_platform:userinfo:crane:"+deviceno;
            redisUtil.set(key,JSONUtil.toJsonStr(userInfoDTO),60*6L);


    }

    private void baojingshuju (String data,ChannelHandlerContext ctx){
        if(!isLogin(ctx)){
            throw  new RuntimeException("未登录");
        }
        //获取内容
        String content  =this.content(data);
        //跳过版本号 0-2
        //采集时间 2-14
        Date  collectTime = new Date();
        //跳过厂家及设备 14-16
        XySession xySession =ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        String number = xySession.getNumber();
        String sn = xySession.getSn();
        String cj  =xySession.getCj();
        TdDTO tdDTO = JSONUtil.toBean(xySession.getProperties(),TdDTO.class);
        StringBuffer sb = new StringBuffer("{")
                //采集时间
                .append("deviceNo:").append(sn(number,sn,cj)).append(",")
                .append("deviceTime:").append(collectTime.getTime()).append(",")
                .append("createTime:").append(collectTime.getTime()).append(",")
                //高度 16-20
                .append("height:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,18,22),16),0.1).toString()).append(",")
                .append("range:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,22,26),16),0.1).toString()).append(",")
                .append("rotaryAngle:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,26,30),16),0.1).toString()).append(",")
                //跳过 起重量 28-32
                //风速
                .append("windSpeed:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,30,34),16),0.1).toString()).append(",")
                //倾角
                .append("tiltAngle:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,34,38),16),0.1).toString()).append(",")
                //重量 根据重量百分比计算
                .append("weight:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,38,40),16),1.0).multiply(tdDTO.getZuidadiaozhong()).setScale(3,BigDecimal.ROUND_HALF_UP).toString()).append(",")
                //力矩百分比
                .append("momentPercentage:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,40,42),16),1.0).toString()).append(",")
                //力矩 根据百分比计算
                .append("moment:").append(toBigDecimal(Integer.parseInt(StrUtil.sub(content,40,42),16),1.0).multiply(tdDTO.getZuidaliju()).setScale(3,BigDecimal.ROUND_HALF_UP).toString()).append(",")
                // 跳过 分速百分比 40-42
                // 跳过 倾斜百分比 42-44
                .append(userinfo(ctx))
                .append(alarm(StrUtil.sub(content,16,18)))
                .append("}");

        try {
            zbusProducerHolder.sendTjDataMessage(sb.toString());
            LOGGER.info(sb.toString());
        } catch (Exception e) {
        }

    }

    private String alarm(String content){
  StringBuffer  sb = new StringBuffer("status:");
     int b = Integer.parseInt(content,16);
     switch (b){
         // 碰撞报警
         case 111 :
         case 112 :
         case 113 :
         case 114 :
          sb.append("11");
          break;
          //碰撞预警
         case 211 :
        case 212 :
        case 213 :
        case 214 :
        sb.append("12");
            break;
            //重量 报警
         case 12 :
             sb.append("1");
             break;
             //重量预警
         case 22 :
             sb.append("2");
             break;
             //力矩报警
         case 13 :
             sb.append("9");
             break;
             //力矩预警
         case 23 :
             sb.append("10");
             break;
             // 倾斜
         case 14 :
             sb.append("16");
             break;
         case 24 :
             break;
             //风速
         case 15 :
             sb.append("15");
             break;
         case 25 :
             break;
             // 高度限位提醒
         case 202 :
         case 102 :
             sb.append("8");
             break;
         // 幅度限位提醒
         case 201 :
         case 101 :
             sb.append("6");
             break;
         default:
             sb.append("0");
             break;
     }
    return sb.toString();

    }
    /**
     *
     * @param source 数据
     * @param coefficient 系数
     * @return
     */
    private BigDecimal toBigDecimal(Integer source,Double coefficient){
        BigDecimal b  = new BigDecimal(source);
        b.setScale(3,BigDecimal.ROUND_HALF_UP);
        BigDecimal c = new BigDecimal(coefficient);
        c.setScale(3,BigDecimal.ROUND_HALF_UP);
        return b.multiply(c).setScale(3,BigDecimal.ROUND_HALF_UP);

    }

    private BigDecimal toBigDecimal(Integer source){
        BigDecimal b  = new BigDecimal(source);
        b.setScale(3,BigDecimal.ROUND_HALF_UP);
        return b;

    }

    /**
     * 比较校验码
     *
     * @param originalCheckCode 收到的数据原始校验码
     * @param data              原始字节
     * @return
     */
    private void equalsIgnoreCaseCheckCode(String originalCheckCode,String  data) {
        byte [] datas = DataUtil.hexStringToBytes(data);
        String checkCodes = doCheckCode(DataUtil.subByteArray(datas, 0, datas.length - 6));
        //计算得到的校验位  累加和的低字节
        String checkCode = checkCodes.substring(checkCodes.length() - 2, checkCodes.length());
        if (!originalCheckCode.equalsIgnoreCase(checkCode)) {
                throw new RuntimeException("校验错误 <"+data+">");
        }
    }

    /**
     * 字节累加和
     *
     * @param b
     * @return
     */
    private static String doCheckCode(byte[] b) {
        short s = 0;
        // 累加求和
        for (byte aB : b) {
            s += aB;
        }
        return DataUtil.byteToHex((byte) s);
    }

    /**
     *  截取校验码
     * @param data
     * @return
     */
    private  String checkCode(String data){
        return StrUtil.sub(data,data.length()-12,data.length()-10);
    }

    /**
     *
     * @param number
     * @param sn
     * @param cj
     * @return
     */
    public  static String sn(String number,String sn ,String cj){
        return Integer.parseInt(sn,16)+"";
    }

    public static String localKey(String type,String sn){
        return type+":"+sn;
    }

    /**
     * 合法性检查
     * @param data
     * @return
     */
    private boolean valid(String data){
        String up = data.toUpperCase();
        if(up.startsWith(HEADER)&&up.endsWith(FOOTER)){
            return true;
        }
        return false;
    }

    /**
     * 帧类型
     * @param data
     * @return
     */
    private String type(String data){
        return StrUtil.sub(data,4,6);
    }

    /**
     * 塔吊编号
     * @param data
     * @return
     */
    private String  number(String data){
        return StrUtil.sub(data,6,8);
    }

    /**
     *
     * @param data
     * @return
     */
    private String sn(String data){
       return StrUtil.sub(data,8,14) ;
    }

    /**
     * 数据体
     * @return
     */
    private String content(String data){
        int length = data.length();
        return StrUtil.sub(data,14,length-12) ;
    }
    private  String time(){
        Date now = new Date();
        StringBuffer sb = new StringBuffer();
        sb.append(Integer.toHexString(DateUtil.year(now)-2000))
                .append(StrUtil.fillBefore(Integer.toHexString(DateUtil.month(now)),'0',2))
                .append(StrUtil.fillBefore(Integer.toHexString(DateUtil.dayOfMonth(now)),'0',2))
                .append(StrUtil.fillBefore(Integer.toHexString(DateUtil.hour(now,true)),'0',2))
                .append(StrUtil.fillBefore(Integer.toHexString(DateUtil.minute(now)),'0',2))
                .append(StrUtil.fillBefore(Integer.toHexString(DateUtil.second(now)),'0',2));
        return sb.toString();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        System.out.println("连接数="+count.incrementAndGet());
        LOGGER.info("###########有设备连接处理器############");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                LOGGER.info("###客户端闲置，服务器主动关闭连接###");
                ctx.close();
            }
        }
    }
    private boolean isLogin(ChannelHandlerContext ctx){
        XySession sn = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        return sn != null;
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("异常[{}]",cause.getMessage());
//        if(!isLogin(ctx)){
//            ctx.close();
//        }
        ctx.close();
    }



    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("连接数="+count.decrementAndGet());
        //获取当前通道session
        XySession currSession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        if(currSession==null){
            return ;
        }
        String localkey = localKey(currSession.getType(),sn(currSession.getNumber(),currSession.getSn(),currSession.getCj()));
        //获取本地缓存中的通道session
        XySession localSession = nettyChannels.get(localkey).attr(NETTY_CHANNEL_KEY).get();
        if(currSession.equals(localSession)){
            //表示本地与当前是同一个副本
            nettyChannels.remove(localkey) ;
            //修改数据库 内设备状态为离线，不管是否操作成功
            try {
            zbusProducerHolder.sendTjLxMessage(localkey);
            }catch (Exception ex){
                LOGGER.error(ex.getMessage());
            }
            LOGGER.info("删除本地通道及redis缓存");
        }else{
            //本地与当前不是同一个副本
            //可能由于设备异常掉线，并重新上线造成
            LOGGER.info("通道副本已被更新");
        }
        LOGGER.info("###########有设备断开处理器############");
    }

    /**
     * 发送测试
     * @param data
     */
    private void  sendTest(String data){
        String number = this.number(data);
        String sn = this.sn(data);
        String cj = this.content(data);


        try {
            zbusProducerHolder.sendTestMessage(sn(number, sn, cj),data);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
