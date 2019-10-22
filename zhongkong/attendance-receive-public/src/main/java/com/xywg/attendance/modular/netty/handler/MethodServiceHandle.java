package com.xywg.attendance.modular.netty.handler;

import com.xywg.attendance.common.model.KeyModel;
import com.xywg.attendance.common.model.TransmissionMessageTemplate;
import com.xywg.attendance.common.utils.RedisUtil;
import com.xywg.attendance.common.utils.UrlUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.xywg.attendance.common.global.GlobalStaticConstant.*;

/**
 * @author hjy
 * @date 2019/2/21
 */
@Service
public class MethodServiceHandle {
    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MethodService methodService;


    @Value("${xywg.communication-private-key}")
    private String communicationPrivateKey;

    private Logger logger = LoggerFactory.getLogger("");

    /**
     * 处理设备初始化
     * /iclock/cdata?SN=CEXA184460007&options=all&language=83&pushver=2.4.0&pushcommkey=jsxywg&PushOptionsFlag=1
     * 说明:sn:表示客户端的序列号  options:表示获取服务器配置参数，目前值只有all  language:表示客户端支持的语言
     * pushver: 表示设备当前最新的push协议版本，新开发的客户端必须支持且必须大于等于
     * PushOptionsFlag: 软件是否支持设备推送配置参数请求，0不支持，1支持，未设置时默认不支持。
     * pushcommkey :该参数用于校验设备是否是 星云网格 的设备,没有此标识的数据一律丢弃
     * @return
     */
    public void handleDeviceInit(ChannelHandlerContext ctx, TransmissionMessageTemplate template) {
        String url = template.getUrl();
        String[] path = url.split("\\?");
        Map<String, String> map = UrlUtil.handleSeparate(path[1], "&", "=");
        if(!map.containsKey("pushcommkey")){
            commonMethod.sendMessage(ctx, "Unknown device ,Parameter 'pushcommkey'  Missing");
            logger.info("Unknown Device: "+map.get("SN")+"  Cause By: No identifier is included 'jsxywg'" );
            return;
        }
        if(!"jsxywg".equals(map.get("pushcommkey"))){
            commonMethod.sendMessage(ctx, "Unknown device ,Parameter 'pushcommkey'  Error");
            logger.info("Unknown Device: "+map.get("SN")+"  Cause By: No identifier is included 'jsxywg'" );
            return;
        }



        StringBuilder res = new StringBuilder();
        res.append("GET OPTION FROM:").append(map.get("SN")).append("\n");
        //设备连接服务器重试间隔
        res.append("ErrorDelay=30").append("\n");
        //获取命令的请求间隔
        res.append("Delay=30").append("\n");
        //客户端定时检查并传送新数据时间（时:分，24小时格式），多个时间用分号分开，最多支持10个时间，如TransTimes=00:00;14:00。
        res.append("TransTimes=00:00;14:05").append("\n");
        //客户端检查并传送新数据间隔时间（分钟），当设置为0时，不检查，如TransInterval=1
        res.append("TransInterval=1").append("\n");
        //服务器支持的协议版本号及时间（时间格式待定）
        res.append("ServerVer=2.4.0").append("\n");
        //服务端依据哪个协议版本开发的
        res.append("PushProtVer=2.4.0").append("\n");
        //客户端向服务器自动上传哪些数据的标识
        res.append("TransFlag=111111111111").append("\n");
        //res.append("TransFlag=TransData AttLog\tOpLog\tAttPhoto\tEnrollUser\tChgUser\tEnrollFP\tChgFP\tFPImag\tFACE\tUserPic\tWORKCODE\tBioPhoto").append("\n");
        //指定服务器所在时区，主要为了同步服务器时间使用
        res.append("TimeZone=8").append("\n");
        //客户端是否实时传送新记录。为1表示有新数据就传送到服务器，为0表示按照TransTimes和TransInterval 规定的时间传送。
        res.append("Realtime=1").append("\n");
        //是否加密传送数据标识，支持通信加密的场合，该参数需设置为1 ,不需要时设置为None
        res.append("Encrypt=1").append("\n");
        //数据加密的标识。1 考勤记录2 操作日志3 考勤照片4 登记新指纹5 登记新用户6 指纹图片7 修改用户信息8 修改指纹9 新登记人脸10 用户照片11 工作号码12 比对照片     位数
        res.append("EncryptFlag=10000000").append("\n");
        //软件是否支持设备推送配置参数请求，0不支持，1支持，未设置时默认不支持。
        res.append("PushOptionsFlag=1").append("\n");
        //软件需要设备推送的参数列表，格式：PushOptions=key1,key2,key3,……,keyN
        res.append("PushOptions=FingerFunOn,FaceFunOn").append("\n");
        //考勤照片base64标识。1：base64编码，其他场合不base64编码。
        res.append("ATTPHOTOBase64=1").append("\n");
        //回复初始化信息
        commonMethod.sendMessage(ctx, res.toString());
    }

    /**
     * 交换公钥
     * /iclock/exchange?SN=$(SerialNumber)&type=publickey
     *
     * @param ctx
     * @param template
     */
    void handleExchangePublicKey(ChannelHandlerContext ctx, TransmissionMessageTemplate template) {
        String path = template.getUrl().split("\\?")[1];
        Map<String, String> map = UrlUtil.handleSeparate(path, "&", "=");
        String sn = map.get("SN");
        String body = template.getBody();

        int index = body.indexOf("=");
        //设备发来的公钥
        String devicePublicKey = body.substring(index + 1, body.length());
        commonMethod.handleDevicePublicKey(communicationPrivateKey, sn, devicePublicKey);
        KeyModel keyModel = (KeyModel) redisUtil.get(DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY + sn);
        commonMethod.sendMessage(ctx, "PublicKey=" + keyModel.getServerPublicKey());
    }

    /**
     * 交换因子
     * /iclock/exchange?SN=$(SerialNumber)&type=factors
     *
     * @param ctx
     * @param template
     */
    void handleExchangeFactor(ChannelHandlerContext ctx, TransmissionMessageTemplate template) {
        String path = template.getUrl().split("\\?")[1];
        Map<String, String> map = UrlUtil.handleSeparate(path, "&", "=");
        String sn = map.get("SN");
        String body = template.getBody();
        int index = body.indexOf("=");
        //设备发来的因子
        String deviceFactors = body.substring(index + 1, body.length());
        commonMethod.handleServerFactors(sn, deviceFactors);
        KeyModel keyModel = (KeyModel) redisUtil.get(DEVICE_SERVER_PUBLIC_KEY_REDIS_KEY + sn);
        commonMethod.sendMessage(ctx, "Factors=" + keyModel.getServerFactors());
    }


    /**
     * 获取命令  这个时候通信方式已经被加密所以 收到的消息  以及回复消息时都是加密数据
     * Get   url =  /iclock/getrequest?SN=${SerialNumber}
     */
    void handleGetCommand(ChannelHandlerContext ctx, TransmissionMessageTemplate template) {
        Map<String, String> map = methodService.handleUrl(template.getUrl());
        String sn = map.get("SN");
        //2分钟过期 用于心跳
        redisUtil.set(DEVICE_INFO_STATUS_REDIS_KEY + sn, sn, 2L);

        //获取redis中存储的设备命令
        List<String> redisCommand = (List<String>) redisUtil.get(DEVICE_GET_COMMAND_REDIS_KEY + sn);

        if (redisCommand == null) {
            //没有时直接回复OK
            //commonMethod.sendMessage(ctx, "OK");
            commonMethod.sendMessageEncrypt(ctx, "OK", sn);
        } else {

            //说明: 因为获取命令为get 请求,每一种浏览器对参数长度都有限制,防止命令串字符超长,故以最大51个命令为切割
            Integer maxSize = 51;

            StringBuffer sb = new StringBuffer();
            List<String> storeToRedis = new ArrayList<>();
            for (int i = 0; i < redisCommand.size(); i++) {
                if (i < maxSize) {
                    sb.append(redisCommand.get(i));
                } else {
                    storeToRedis.add(redisCommand.get(i));
                }
            }
            //commonMethod.sendMessage(ctx, sb.toString());
            commonMethod.sendMessageEncrypt(ctx, sb.toString(), sn);
            if (storeToRedis.size() > 0) {
                //将未下发的命令依然存放到redis中,下一次再下发
                redisUtil.set(DEVICE_GET_COMMAND_REDIS_KEY + sn, storeToRedis);
            } else {
                //命令下发完成后清空redis中该设备的数据
                redisUtil.remove(DEVICE_GET_COMMAND_REDIS_KEY + sn);
            }
        }
    }
}
