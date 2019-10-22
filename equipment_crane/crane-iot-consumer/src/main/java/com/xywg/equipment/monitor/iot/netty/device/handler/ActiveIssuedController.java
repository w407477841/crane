package com.xywg.equipment.monitor.iot.netty.device.handler;

import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.core.util.FtpUtils;
import com.xingyun.crane.cache.RedisUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.xywg.equipment.monitor.iot.netty.device.handler.CommonStaticMethod.stringToHexString;

/**
 * @author hjy
 * @date 2018/10/8
 * 设备主动下发操作(扬尘,塔吊)
 */
@RestController
@RequestMapping("/ssdevice/activeIssued")
public class ActiveIssuedController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    protected XywgProerties xywgProerties;
    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    private FtpUtils ftpUtils;

    private Logger logger = LoggerFactory.getLogger(ActiveIssuedController.class);

    /**
     * 发送升级指令 功能码0005
     */
    @PostMapping("/upgradePackage")
    @SuppressWarnings("all")
    public String upgradePackage(@RequestBody Map<String, Object> map) {
        StringBuilder resStr = new StringBuilder();
        try {
            String sns = map.get("codes").toString();
            String path = map.get("path").toString();

            String ftpPath = path.substring(0, path.lastIndexOf("/"));
            String fileName = path.substring(path.lastIndexOf("/") + 1);

            //long fileLength = ftpUtils.getFileSize(ftpPath, fileName);
            boolean b = ftpUtils.downloadFile(ftpPath, fileName, xywgProerties.getUpgradeFileBasePath() + ftpPath);
            if (!b) {
                logger.info("<<从ftp下载文件失败,path:" + path + ">>");
                return "error";
            }
            long fileLength = new File(xywgProerties.getUpgradeFileBasePath() + path).length();
            //文件长度转16进制
            String fileLength16 = Long.toHexString(fileLength).length() % 2 == 0 ?
                    Long.toHexString(fileLength) : ("0" + Long.toHexString(fileLength));

            String[] list = sns.split(",");
            for (String sn : list) {
                //序列号转16进制字节码
                String snCode = stringToHexString(sn);
                Channel channel = NettyChannelManage.getChannel(sn);
                if (channel != null) {
                    LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
                    linkedMap.put("02", fileLength16);
                    commonMethod.resMessageJoint(channel, snCode, linkedMap, "0005", "01", "00");
                    resStr.append(sn).append(",");
                    redisUtil.set(xywgProerties.getRedisHead() + ":upgradeSn_" + sn, path);
                }
            }
            return resStr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 设备重启指令下发  功能码0009
     *
     * @return
     */
    @PostMapping("/rebootDevice")
    public String rebootDevice(@RequestBody Map<String, Object> map) {
        return reboot(map, "0009");
    }




    /**
     * 设备重启指令下发
     *
     * @return
     */
    private String reboot(Map<String, Object> map, String command) {
        try {
            if (!map.containsKey("deviceNoList")) {
                return "设备编号参数deviceNoList缺失,以逗号分隔的字符串";
            }
            String[] deviceNoList = map.get("deviceNoList").toString().split(",");
            StringBuilder res = new StringBuilder();
            for (String deviceNo : deviceNoList) {
                Channel channel = NettyChannelManage.getChannel(deviceNo);
                if (channel != null) {
                    //序列号转16进制字节码
                    String snCode = stringToHexString(deviceNo).replaceAll(" ", "");
                    commonMethod.resMessageJoint(channel, snCode, null, command, "01", "00");
                    res.append(deviceNo).append(",");
                }
            }
            return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("alipay")
    public Object alipay(String userName,String type,String content){
        commonMethod.userMsg(userName,type,content);
        return "ok";
    }

}



