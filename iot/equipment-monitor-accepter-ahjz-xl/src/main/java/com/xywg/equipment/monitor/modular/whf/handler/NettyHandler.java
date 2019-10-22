package com.xywg.equipment.monitor.modular.whf.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.equipment.monitor.config.ZbusProducerHolder;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.DataUtil;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.model.*;
import com.xywg.equipment.monitor.modular.whf.server.*;
import com.xywg.equipment.monitor.modular.whf.server.impl.ProjectUnloadServiceImpl;
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
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
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
public class NettyHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyHandler.class);
    private static AtomicInteger count = new AtomicInteger();
    private static final String HEADER = "A55A";
    private static final String FOOTER = "CC33C33C";
    @Autowired
    ZbusProducerHolder zbusProducerHolder;
    /**
     * 绑定通道
     */
    private static final AttributeKey<XySession> NETTY_CHANNEL_KEY = AttributeKey.valueOf("netty.channel");
    /**
     * 本地通道缓存
     */
    public static Map<String, Channel> nettyChannels = new ConcurrentHashMap<>(10);
    @Autowired
    IProjectUnloadDetailService iProjectUnloadDetailService;
    @Autowired
    IProjectUnloadService iProjectUnloadService;
    @Autowired
    IProjectInfoService iProjectInfoService;
    @Autowired
    IProjectUnloadHeartbeatService iProjectUnloadHeartbeatService;
    @Autowired
    IProjectUnloadAlarmService iProjectUnloadAlarmService;
    @Autowired
    XywgProerties xywgProerties;
    @Autowired
    RedisUtil redisUtil;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) {

        //可读长度
        int readableLength = buf.readableBytes();
        byte[] msgData = new byte[readableLength];
        buf.readBytes(msgData);
        //接受的信息
        String originalData = DataUtil.bytesToHexString(msgData).toUpperCase();
        originalData = originalData.substring(originalData.indexOf(HEADER));
        System.out.println(originalData);
        //数据合法性检查
        if (!valid(originalData)) {
            throw new RuntimeException("非法数据 <" + originalData + ">");
        }
        sendTest(originalData);
        //类型
        String type = this.type(originalData);
        switch (type) {
            // 心跳包/未注册时注册，已注册则返回心跳包
            case "01":
                this.xintiao(originalData, ctx);
                break;
            // 标定信息桢
            case "04":
                this.standardInfo(originalData, ctx);
                break;
            //实时工况数据
            case "10":
                this.realTimeData(originalData, ctx, 10);
                break;
            //报警信息
            case "11":
                this.realTimeData(originalData, ctx, 11);
                break;
        }
    }

    /**
     * 十六进制转换时间格式
     *
     * @param hex 十六进制数据
     * @return 返回转换后日期
     */
    private String time(String hex) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(2000 + Integer.parseInt(StrUtil.sub(hex, 0, 2), 16))
                .append(StrUtil.fillBefore(Integer.parseInt(StrUtil.sub(hex, 2, 4), 16) + "", '0', 2))
                .append(StrUtil.fillBefore(Integer.parseInt(StrUtil.sub(hex, 4, 6), 16) + "", '0', 2))
                .append(StrUtil.fillBefore(Integer.parseInt(StrUtil.sub(hex, 6, 8), 16) + "", '0', 2))
                .append(StrUtil.fillBefore(Integer.parseInt(StrUtil.sub(hex, 8, 10), 16) + "", '0', 2))
                .append(StrUtil.fillBefore(Integer.parseInt(StrUtil.sub(hex, 10, 12), 16) + "", '0', 2));
        return stringBuffer.toString();
    }

    /**
     * 判断登录状态
     *
     * @param ctx 上下文对象
     * @return 登录状态
     */
    private boolean isLogin(ChannelHandlerContext ctx) {
        XySession sn = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        return sn != null;
    }

    /**
     * 截取校验码
     *
     * @param data 接收的数据
     * @return 返回的校验码
     */
    private String checkCode(String data) {
        return StrUtil.sub(data, data.length() - 12, data.length() - 10);
    }

    /**
     * 比较校验码
     *
     * @param originalCheckCode 收到的数据原始校验码
     * @param data              原始字节
     */
    private void equalsIgnoreCaseCheckCode(String originalCheckCode, String data) {
        byte[] datas = DataUtil.hexStringToBytes(data);
        String checkCodes = doCheckCode(DataUtil.subByteArray(datas, 0, datas.length - 6));
        //计算得到的校验位  累加和的低字节
        String checkCode = checkCodes.substring(checkCodes.length() - 2);
        if (!originalCheckCode.equalsIgnoreCase(checkCode)) {
            throw new RuntimeException("校验错误 <" + data + ">");
        }
    }

    /**
     * 心跳包/未登录时登录
     * @param data 接受到的信息
     * @param ctx  上下文对象
     */
    private void xintiao(String data, ChannelHandlerContext ctx) {
        //验证码
        String checkCode = this.checkCode(data);
        //校验
        this.equalsIgnoreCaseCheckCode(checkCode, data);
        //判断是否注册，若未注册则执行注册操作
        //预留栏位
        String number = this.number(data);
        //黑匣子号
        String sn = this.sn(data);
        //信息体
        String cj = this.content(data);
        //如果未登录，执行登录操作
        if (!isLogin(ctx)) {
            //进入缓存，查看当前连接是否已经存在，如果不存在，执行注册操作
            Channel obj = nettyChannels.get(localKey("xl", sn(number, sn, cj)));
            if (obj == null) {
                XySession xySession = new XySession();
                xySession.setSn(sn);
                xySession.setType("xl");
                xySession.setCj(cj);
                xySession.setNumber(number);
                xySession.setId(RandomUtil.simpleUUID());
                //双向绑定
                ctx.channel().attr(NETTY_CHANNEL_KEY).set(xySession);
                nettyChannels.put(localKey("xl", sn(number, sn, cj)), ctx.channel());
                //上线
                online(sn);
            } else {
                //如果连接已经存在，断开连接
                ctx.close();
                obj.close();
            }
        } else {
            //返回心跳包
            String ret = HEADER + "02" + number + sn + "010A" + this.time();
            String retChechCode = doCheckCode(DataUtil.hexStringToBytes(ret));
            String length = length(ret);
            ret = ret + retChechCode + length + FOOTER;
            ctx.writeAndFlush(Unpooled.copiedBuffer(DataUtil.hexStringToBytes(ret)));
        }


    }

    /**
     * 上线
     * @param deviceNo 黑匣子编号
     */
    private void online(String deviceNo) {

            try{
                ProjectUnloadHeartbeat heartbeat = new ProjectUnloadHeartbeat();
                //黑匣子编号
                heartbeat.setDeviceNo(deviceNo);
                // 获取设备信息
                Wrapper<ProjectUnload> wrapper = new EntityWrapper<>();
                wrapper.eq(StrUtil.isNotBlank(heartbeat.getDeviceNo()), "device_no", heartbeat.getDeviceNo());
                //获取设备信息
                ProjectUnload unload = getUnload(deviceNo);
                heartbeat.setUnloadId(unload.getId());
                //数据创建时间
                heartbeat.setCreateTime(new Date());
                //操作心跳表
                iProjectUnloadHeartbeatService.doOpenBusiness(heartbeat);
                //1.修改数据库 online 字段
                ProjectUnload projectUnload = new ProjectUnload();
                //同步id
                projectUnload.setId(unload.getId());
                //在线状态
                projectUnload.setIsOnline(1);
                //更新设备在线状态
                iProjectUnloadService.updateById(projectUnload);
                //删除 设备信息缓存
                String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectUnloadServiceImpl.DEVICE_INFO_SUFF + ":" + deviceNo;
                System.out.println(deviceKey);
                redisUtil.remove(deviceKey);
            }
            catch(Exception e){
                e.printStackTrace();
            }


    }

    /**
     * 根据设备号获取设备
     * @param deviceNo 黑匣子编号
     * @return 返回符合条件的设备对象
     */
    private ProjectUnload getUnload(String deviceNo)
    {
        Wrapper<ProjectUnload> wrapper = new EntityWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(deviceNo), "device_no", deviceNo);
        wrapper.eq("is_del",0);
        //获取设备信息
        ProjectUnload unload;
            unload= iProjectUnloadService.selectOne(wrapper);
        if (unload == null) {
            // 压根没有这个设备
            throw new RuntimeException("数据库中没有" + deviceNo + "该设备");
        }
        if (0 == unload.getStatus()) {
            throw new RuntimeException("" + deviceNo + "该设备未启用");
        }
        ProjectInfo projectInfo = iProjectInfoService.selectById(unload.getProjectId());
        if (projectInfo == null) {
            throw new RuntimeException("设备" + deviceNo + "不在任何项目下");
        }
        return unload;
    }
    /**
     * 下线
     */
    private void offline(String deviceNo) {
        //获取设备信息
        // 获取 设备信息
        ProjectUnload unload=getUnload(deviceNo);
        // 更新 受控数据的截止时间
        ProjectUnloadHeartbeat projectUnloadHeartbeat = new ProjectUnloadHeartbeat();
        //黑匣子编号
        projectUnloadHeartbeat.setDeviceNo(deviceNo);
        //数据创建时间
        projectUnloadHeartbeat.setCreateTime(new Date());
        iProjectUnloadHeartbeatService.updateEndTime(projectUnloadHeartbeat);
        //1.修改数据库 online 字段
        ProjectUnload projectUnload = new ProjectUnload();
        projectUnload.setId(unload.getId());
        //在线状态设置为离线
        projectUnload.setIsOnline(0);
        iProjectUnloadService.updateById(projectUnload);
        //删除 设备信息缓存
        String deviceKey = xywgProerties.getRedisHead() + ":" + ProjectUnloadServiceImpl.DEVICE_INFO_SUFF + ":" + deviceNo;
        redisUtil.remove(deviceKey);

    }

    /**
     * 数据长度
     *
     * @param data 数据
     * @return 长度
     */
    private static String length(String data) {
        int s = data.length() / 2;
        String length = Integer.toHexString(s);
        length = StrUtil.fillBefore(length, '0', 2);
        return length;
    }

    /**
     * 标定信息桢
     *
     * @param data 接收的数据
     * @param ctx  上下文对象
     */
    private void standardInfo(String data, ChannelHandlerContext ctx) {
        if (!isLogin(ctx)) {
            throw new RuntimeException("未登录");
        }
        //黑匣子编号
        String deviceNo = String.valueOf(Integer.parseInt(StrUtil.sub(data, 8, 14), 16));
        Wrapper<ProjectUnload> wrapper = new EntityWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(deviceNo), "device_no", deviceNo);
        //获取设备信息
        ProjectUnload unload = getUnload(deviceNo);
        //获取内容
        String content = this.content(data);
        //重量空载AD值
        BigDecimal noLoadWeightAD = toBigDecimal(Integer.parseInt(StrUtil.sub(content, 0, 4), 16));
        unload.setNoLoadWeightAD(noLoadWeightAD);
        //重量空载实际值
        BigDecimal noLoadWeight = toBigDecimal(Integer.parseInt(StrUtil.sub(content, 4, 8), 16));
        unload.setNoLoadWeight(noLoadWeight);
        //重量负载AD值
        BigDecimal loadWeightAD = toBigDecimal(Integer.parseInt(StrUtil.sub(content, 8, 12), 16));
        unload.setLoadWeightAD(loadWeightAD);
        //重量负载实际值
        BigDecimal loadWeight = toBigDecimal(Integer.parseInt(StrUtil.sub(content, 12, 16), 16));
        unload.setLoadWeight(loadWeight);
        iProjectUnloadService.update(unload,wrapper);
    }

    /**
     * 实时工况数据/报警信息
     *
     * @param data 接受到的数据
     * @param ctx  上下文对象
     */
    private void realTimeData(String data, ChannelHandlerContext ctx, Integer index) {
        if (!isLogin(ctx)) {
            throw new RuntimeException("未登录");
        }
        //获取内容
        String content = this.content(data);
        //获取黑匣子编号
        String deviceNo = String.valueOf(Integer.parseInt(StrUtil.sub(data, 8, 14), 16));
        //获取黑匣子编号对应设备
        ProjectUnload unload=getUnload(deviceNo);
        //创建新的实时数据记录
        ProjectUnloadDetail projectUnloadDetail = new ProjectUnloadDetail();
        dataAction(unload,projectUnloadDetail,content);
        //若为报警信息
        if (index != null && index == 11) {
            //执行报警操作
            alarmAction(projectUnloadDetail,content);
        }
    }

    /**
     *
     * @param unload 卸料设备
     * @param projectUnloadDetail 新建的卸料实时数据
     * @param content 收到的信息
     */
    private void dataAction(ProjectUnload unload,ProjectUnloadDetail projectUnloadDetail,String content)
    {
        //黑匣子编号
        projectUnloadDetail.setDeviceNo(unload.getDeviceNo());
        //卸料平台id
        projectUnloadDetail.setUnloadId(unload.getId());
        //时间
        String time = time(StrUtil.sub(content, 0, 12));
        //获取实时吊重
        BigDecimal realTimeLift = toBigDecimal(Integer.parseInt(StrUtil.sub(content, 12, 16), 16));
        projectUnloadDetail.setWeight(realTimeLift);
        //获取重量百分比
        BigDecimal weightPercent = toBigDecimal(Integer.parseInt(StrUtil.sub(content, 16, 18), 16));
        projectUnloadDetail.setWeightPercentage(weightPercent);
        //实时倾斜度1
        BigDecimal realTimeTilt1 = toBigDecimal(Integer.parseInt(StrUtil.sub(content, 18, 22), 16))
                .subtract(new BigDecimal("1500")).abs().divide(new BigDecimal("100"), 2, RoundingMode.UP);
        projectUnloadDetail.setTiltAngle1(realTimeTilt1);
        //实时倾斜百分比1
        BigDecimal tiltPercent1 = toBigDecimal(Integer.parseInt(StrUtil.sub(content, 22, 24), 16));
        projectUnloadDetail.setTiltPercent1(tiltPercent1);
        //实时倾斜度2
        BigDecimal realTimeTilt2 = toBigDecimal(Integer.parseInt(StrUtil.sub(content, 24, 28), 16))
                .subtract(new BigDecimal("1500")).abs();
        projectUnloadDetail.setTiltAngle2(realTimeTilt2);
        //实时倾斜百分比2
        BigDecimal tiltPercent2 = toBigDecimal(Integer.parseInt(StrUtil.sub(content, 28, 30), 16));
        projectUnloadDetail.setTiltPercent2(tiltPercent2);
        projectUnloadDetail.setCreateTime(new Date());
        projectUnloadDetail.setDeviceTime(DateUtil.parse(time,"yyyyMMddHHmmss").toString());
        try{
            iProjectUnloadDetailService.insert(projectUnloadDetail);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 执行报警操作
     * @param projectUnloadDetail 对应的实时数据
     * @param content 信息
     */
    private void alarmAction(ProjectUnloadDetail projectUnloadDetail,String content)
    {
        //若为报警信息桢，则需要获取报警原因和报警级别
        ProjectUnloadAlarm projectUnloadAlarm=new ProjectUnloadAlarm();
        //黑匣子编号
        projectUnloadAlarm.setDeviceNo(projectUnloadDetail.getDeviceNo());
        //报警/类型原因
        projectUnloadAlarm.setAlarmId(Integer.parseInt(StrUtil.sub(content, 30, 32), 16));
        //查询对应的实时数据对象，获取实时数据id
        Wrapper<ProjectUnloadDetail> detailWrapper=new EntityWrapper<>();
        detailWrapper.setSqlSelect("id as id");
        detailWrapper.eq("device_no",projectUnloadDetail.getDeviceNo());
        detailWrapper.eq("device_time",projectUnloadDetail.getDeviceTime());
        List<ProjectUnloadDetail> detailList=iProjectUnloadDetailService.selectList(detailWrapper);
        if(detailList!=null&&detailList.size()>0)
        {
            projectUnloadAlarm.setDetailId(detailList.get(0).getId());
        }
        //卸料id
        projectUnloadAlarm.setUnloadId(projectUnloadDetail.getUnloadId());
        projectUnloadAlarm.setCreateTime(new Date());
        //报警级别
        projectUnloadAlarm.setAlarmClass(Integer.parseInt(StrUtil.sub(content, 32, 34), 16));
        iProjectUnloadAlarmService.insert(projectUnloadAlarm);
    }
    /**
     * Integer类型转BigDecimal类型
     *
     * @param source 参数
     * @return 转换的结果
     */
    private BigDecimal toBigDecimal(Integer source) {
        BigDecimal b = new BigDecimal(source);
        b.setScale(3, BigDecimal.ROUND_HALF_UP);
        return b;
    }

    /**
     * 字节累加和
     *
     * @param b 字节数组
     * @return 返回的字节数组长度
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
     * 拼接设备类型和黑匣子号
     *
     * @param type type
     * @param sn   sn
     * @return 返回拼接后的值
     */
    private static String localKey(String type, String sn) {
        return type + ":" + sn;
    }

    /**
     * 合法性检查
     *
     * @param data 需检查的数据
     * @return 是否合格
     */
    private boolean valid(String data) {
        String up = data.toUpperCase();
        return up.startsWith(HEADER) && up.endsWith(FOOTER);
    }

    /**
     * 帧类型
     *
     * @param data 数据
     * @return 返回的类型
     */
    private String type(String data) {
        return StrUtil.sub(data, 4, 6);
    }

    /**
     * 预留栏位
     *
     * @param data 数据
     * @return 返回的塔吊编号
     */
    private String number(String data) {
        return StrUtil.sub(data, 6, 8);
    }

    /**
     * 获取黑匣子编号
     *
     * @param data 获取的数据
     * @return 黑匣子编号
     */
    private String sn(String data) {
        return String.valueOf(Integer.parseInt(StrUtil.sub(data, 8, 14),16));
    }

    /**
     * 数据体
     *
     * @return 获取的数据中信息部分
     */
    private String content(String data) {
        int length = data.length();
        return StrUtil.sub(data, 14, length - 12);
    }

    /**
     * 获取当前时间
     *
     * @return 返回当前时间
     */
    private String time() {
        Date now = new Date();
        StringBuffer sb = new StringBuffer();
        sb.append(Integer.toHexString(DateUtil.year(now) - 2000))
                .append(StrUtil.fillBefore(Integer.toHexString(DateUtil.month(now) + 1), '0', 2))
                .append(StrUtil.fillBefore(Integer.toHexString(DateUtil.dayOfMonth(now)), '0', 2))
                .append(StrUtil.fillBefore(Integer.toHexString(DateUtil.hour(now, true)), '0', 2))
                .append(StrUtil.fillBefore(Integer.toHexString(DateUtil.minute(now)), '0', 2))
                .append(StrUtil.fillBefore(Integer.toHexString(DateUtil.second(now)), '0', 2));
        return sb.toString();
    }

    /**
     * 获取连接数
     *
     * @param ctx 上下文对象
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("连接数=" + count.incrementAndGet());
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

    /**
     * 获取异常并处理
     *
     * @param ctx   上下文对象
     * @param cause 异常对象
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("异常[{}]", cause.getMessage());
        ctx.close();
    }

    /**
     * 离线
     * @param ctx 上下文对象
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("连接数=" + count.decrementAndGet());
        //获取当前通道session
        XySession currSession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        if (currSession == null) {
            return;
        }
        String localkey = localKey(currSession.getType(), sn(currSession.getNumber(), currSession.getSn(), currSession.getCj()));
        //获取本地缓存中的通道session
        XySession localSession = nettyChannels.get(localkey).attr(NETTY_CHANNEL_KEY).get();
        if (currSession.equals(localSession)) {
            //表示本地与当前是同一个副本
            nettyChannels.remove(localkey);
            //修改数据库 内设备状态为离线，不管是否操作成功

                //下线
                offline(localSession.getSn());

            LOGGER.info("删除本地通道及redis缓存");
        } else {
            //本地与当前不是同一个副本
            //可能由于设备异常掉线，并重新上线造成
            LOGGER.info("通道副本已被更新");
        }
        LOGGER.info("###########有设备断开处理器############");
    }

    /**
     * 发送测试
     *
     * @param data 测试数据
     */
    private void sendTest(String data) {
        String number = this.number(data);
        String sn = this.sn(data);
        String cj = this.content(data);


        try {
            zbusProducerHolder.sendTestMessage(sn(number, sn, cj), data);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * @param number number
     * @param sn     sn
     * @param cj     cj
     * @return 返回值
     */
    private static String sn(String number, String sn, String cj) {
        System.out.println(number + cj);
        return Integer.parseInt(sn, 16) + "";
    }

}