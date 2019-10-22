package com.xywg.attendance.modular.handler;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.attendance.common.global.UploadFlagEnum;
import com.xywg.attendance.common.model.DataMessage;
import com.xywg.attendance.common.model.TransmissionMessageTemplate;
import com.xywg.attendance.common.utils.DateUtils;
import com.xywg.attendance.common.utils.FastDfsUtil;
import com.xywg.attendance.common.utils.UrlUtil;
import com.xywg.attendance.modular.business.model.*;
import com.xywg.attendance.modular.business.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.xywg.attendance.common.global.GlobalStaticConstant.SEPARATOR_EQUAL_SIGN;
import static com.xywg.attendance.common.global.GlobalStaticConstant.SEPARATOR_WITH;

/**
 * @author hjy
 * @date 2019/2/21
 */
@Service
public class MethodServiceHandle {

    @Autowired
    private AttendanceRecordService attendanceRecordService;
    @Autowired
    private AmInfService amInfService;
    @Autowired
    private PerInfService perInfService;
    @Autowired
    private DeviceExceptionRecordService exceptionRecordService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private DeviceCommandService deviceCommandService;


    private Logger logger = LoggerFactory.getLogger("");

    /**
     * 上传更新信息
     * 主要上传客户端的固件版本号、登记用户数、登记指纹数、考勤记录数、设备IP地址、指纹算法版本、人脸算法版本、
     * 注册人脸所需人脸个数、登记人脸数、设备支持功能标示信息
     * Get请求
     * 示例数据:iclock/getrequest?SN=CEXA184460007&INFO=1.0.78,2,0,0,192.168.20.21,12,3,12,0,11110
     * *  info中分别表示了 固件版本号,登记用户数,登记指纹数,考勤记录数,设备IP地址,指纹算法版本,人脸算法版本,注册人脸所需人脸个数,登记人脸数
     * *   ,设备支持功能标示(0表示不支持,1支持)
     * *   设备支持功能标示:
     * *       1 指纹功能
     * *       2 人脸功能
     * *       3 用户照片功能
     * *       4 比对照片功能（支持比对照片功能，参数BioPhotoFun需要设置为1）
     * *       5 可见光人脸模板功能（支持人脸模板该功能，参数BioDataFun需要设置为1）
     *
     * @param msg
     */
    public void uploadUpdateInformation(TransmissionMessageTemplate msg) {

        Map<String, String> map = handleUrl(msg.getUrl());
        String sn = map.get("SN");
        String info = map.get("INFO");
        String[] values = info.split(",");

        //固件版本
        String version = values[0];
        //设备IP地址
        String ip = values[4];
        //指纹算法版本
        String arithmeticVersion = values[5];


        //根据业务不同处理不同
        if (values.length > 9) {
            //laborSystem.saveDeviceInfo(sn, version, ip, arithmeticVersion);
        }
    }

    /**
     * 处理上传考勤记录
     * 请求的url:  /iclock/cdata?SN=0316144680030&table=ATTLOG&Stamp=9999 HTTP/1.1
     * body参数例子:
     * 1452 2015-07-30 15:16:28 0 1 0 0 0
     * 1452 2015-07-30 15:16:29 0 1 0 0 0
     * 1452 2015-07-30 15:16:30 0 1 0 0 0
     * 一行数据分别代表:  工号  考勤时间  考勤状态  验证方式  workcode编码  预留字段  预留字段
     *
     * @param msg url 中所带参数
     */
    public void handleUploadAttendance(TransmissionMessageTemplate msg) {
        String body = msg.getBody();
        //一共拥有的考勤记录条数bodyMessage.length(一行为一条考勤数据)
        String[] bodyMessage = body.split("\n");
        //设备编号
        String sn = getSn(msg.getUrl());

        //List<AttendanceRecord> toDbList = new ArrayList<>();
        for (String aBodyMessage : bodyMessage) {
            if (StringUtils.isBlank(aBodyMessage.trim())) {
                continue;
            }
            //params 代表一行数据   工号  考勤时间  考勤状态  验证方式  workcode编码  预留字段  预留字段
            //1452	2015-07-30 15:16:28	0	1	0	0	0
            String[] params = aBodyMessage.split("\t");
            //身份证
            String idNo = params[0];
            String attendanceTime = params[1];
            Wrapper<AttendanceRecord> perWp = new EntityWrapper<>();
            perWp.eq("ID_NO", idNo);
            perWp.eq("Record", attendanceTime);
            //查询正常考勤数据里是否存在
            AttendanceRecord attendanceRecordList = attendanceRecordService.selectOne(perWp);
            if (attendanceRecordList != null) {
                continue;
            }
            Wrapper<DeviceExceptionRecord> perER = new EntityWrapper<>();
            perER.eq("id_card_number", idNo);
            perER.eq("time", attendanceTime);
            //查询异常考勤数据里是否存在
            DeviceExceptionRecord deviceExceptionRecord = exceptionRecordService.selectOne(perER);

            //有可能是考勤图片先处理
            if (deviceExceptionRecord != null) {
                continue;
            }


            /**
             * 处理人员不存在时
             */
            Wrapper<PerInf> perInfEw = new EntityWrapper<>();
            perInfEw.eq("Id_No", idNo);
            // 查询人员信息  苏中要求一个人只能在一个项目下  所以先查询人员 根据人员找出项目编号
            PerInf perInf = perInfService.selectOne(perInfEw);
            Wrapper<AmInf> amInfEw = new EntityWrapper<>();
            amInfEw.eq("AM_Code", sn);
            //查询设备信息
            List<AmInf> amInfList = amInfService.selectList(amInfEw);
            Integer atType = null;
            if (amInfList.size() > 0) {
                atType = amInfList.get(0).getAtType();
            }
            if (perInf == null) {
                DeviceExceptionRecord record = new DeviceExceptionRecord(
                        sn, 1, idNo, DateUtils.parseDate(attendanceTime),
                        null, new Date(), 2, 0, atType);
                //人员不存在 存储异常考勤
                exceptionRecordService.insertSqlServer(record);
                continue;
            }

            Wrapper<AmInf> amInfEwNew = new EntityWrapper<>();
            amInfEwNew.eq("AM_Code", sn);
            amInfEwNew.eq("Project_Code", perInf.getProjectCode());
            //查询设备信息
            AmInf amInf = amInfService.selectOne(amInfEwNew);
            if (amInf == null) {
                DeviceExceptionRecord record = new DeviceExceptionRecord(
                        sn, 1, idNo, DateUtils.parseDate(attendanceTime),
                        null, new Date(), 1, 0, atType);
                //设备不存在  存储异常考勤
                exceptionRecordService.insertSqlServer(record);
                continue;
            }

            //正常考勤
            AttendanceRecord attendanceRecord = new AttendanceRecord(sn, amInf.getProjectCode(), DateUtils.parseDate(attendanceTime), amInf.getAtType(), idNo);
            attendanceRecordService.insertSqlServer(attendanceRecord);
        }

    }


    /**
     * 处理上传考勤图片
     * url:  /iclock/cdata?SN=${SerialNumber}&table=ATTPHOTO&Stamp=${XXX} HTTP/1.1
     * body: PIN=20150731103012-123.jpg SN=0316144680030 size=9512 CMD=uploadphoto picture=akksssaaqwfvsssdfghgessf
     * body部分的数据以\n 分隔  参数分别为:图片名称,设备编号,图片的大小,命令类型,图片的base64字符串
     * 其中图片名称 横杠前的为考勤时间,横杠后面的为人员(身份证信息)
     *
     * @param msg 中所带参数
     */
    public void handleUploadAttendancePhoto(TransmissionMessageTemplate msg) {
        //设备编号
        String sn = getSn(msg.getUrl());
        String[] bodys = msg.getBody().split("\nCMD=uploadphoto\npicture=");
        if (bodys.length > 1) {
            Map<String, String> params = UrlUtil.handleSeparate(bodys[0], "\n", "=");
            if (null != params) {
                String base64Photo = bodys[1];
                //保存考勤图片到fastDfs 返回存储的地址
                // String url = fastDfsUtil.uploadFile(base64Photo, "jpg");
                //处理文件名得到考勤时间和考勤人员的身份证号码
                String[] pins = params.get("PIN").replace(".jpg", "").split("-");
                if (pins.length > 1) {
                    //用户身份证号
                    String userIdNo = pins[1];
                    //考勤时间
                    String timeParam = DateUtils.getDate(pins[0]);
                    businessService.handleAttendance(userIdNo, DateUtils.parseDate(timeParam), sn, base64Photo);
                }
            }
        }

    }

    /**
     * 命令回复
     * url:  /iclock/devicecmd?SN=${SerialNumber}
     * body:   ID=info8487&Return=0&CMD=DATA
     * ID=info8488&Return=0&CMD=DATA
     * <p>
     * body部分分别代表的含义:  服务器下发命令的命令编号,执行结果,服务器下发命令的命令描述
     *
     * @param msg
     */
    public void handleCommandResponse(TransmissionMessageTemplate msg) {
        String body = msg.getBody();
        String[] lines = body.split("\n");
        String sn = getSn(msg.getUrl());
        if (lines.length > 0) {
            for (String line1 : lines) {
                if (StringUtils.isBlank(line1.trim())) {
                    continue;
                }
                Map<String, String> line = UrlUtil.handleSeparate(line1, "&", "=");
                String uuid = line.get("ID");
                //执行结果
                Integer returnRes = Integer.parseInt(line.get("Return")) == 0 ? 1 : Integer.parseInt(line.get("Return"));
                DeviceCommand deviceCommand = new DeviceCommand(sn, returnRes, new Date(), new Date(), uuid);

                Wrapper<DeviceCommand> perInfEw = new EntityWrapper<>();
                perInfEw.eq("uuid", uuid);
                deviceCommandService.update(deviceCommand, perInfEw);
            }
        }

    }


    public Map<String, String> handleUrl(String url) {
        String[] path = url.split("\\?");
        if (!url.contains(SEPARATOR_WITH) && !url.contains(SEPARATOR_EQUAL_SIGN)) {
            return null;
        }

        Map<String, String> map = new HashMap<>(1);
        if (path[1].contains(SEPARATOR_WITH)) {
            map = UrlUtil.handleSeparate(path[1], SEPARATOR_WITH, SEPARATOR_EQUAL_SIGN);
        } else {
            String[] split = path[1].split(SEPARATOR_EQUAL_SIGN);
            map.put(split[0], split[1]);
        }
        return map;
    }


    public String getSn(String url) {
        Map<String, String> map = handleUrl(url);
        if (map == null) {
            return "";
        }
        if (!map.containsKey("SN")) {
            return "";
        }
        return map.get("SN");
    }

    //  -------------------------------------------------------------------------------------------------------------

    /**
     * 该url 中所有的方法为设备新增的数据上传(如:在设备端新增了一个考勤人员)
     * 如果业务不允许设备端主动添加删除人员等操作,那么该方法弃用
     * table=OPERLOG相同URL
     * POST /iclock/cdata?SN=${SerialNumber}&table=OPERLOG&Stamp=${XXX} HTTP/1.1
     *
     * @param msg 中所带参数
     */
    public void handleTableOperLog(TransmissionMessageTemplate msg) {
        List<DataMessage> bodys = UrlUtil.handleSeparateList(msg.getBody(), "\n", " ", "\t", "=");

        //说明: 因为上传数据包时,里面包含了各种操作数据,所以必须循环选择处理
        for (DataMessage dataMessage : bodys) {
            UploadFlagEnum uploadFlagEnum = UploadFlagEnum.getUploadFlagEnum(dataMessage.getKeyName());
            if (uploadFlagEnum == null) {
                logger.info("OPERLOG ---> There is no match to the associated URL regular expression: " + msg.getBody());
                continue;
            }
            switch (uploadFlagEnum) {
                //上传用户照片(设备新增记录)
                case USERPIC:
                    handleUploadUserPhoto(msg);
                    break;
                //上传比对照片(设备新增记录)
                case BIOPHOTO:
                    handleUploadContrastPhoto(msg);
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 获取命令
     * url: /iclock/getrequest?SN=${SerialNumber}
     * body部分没有数据
     *
     * @param msg
     */
    public void handleGetCommand(TransmissionMessageTemplate msg) {
        //这里可以用于 管理设备在线离线
        //设备编号
        String sn = getSn(msg.getUrl());


    }


    /**
     * 上传用户照片 (用户头像)
     * 示例数据:USERPIC PIN=1002	FileName=1002.jpg	Size=36288	Content=/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDABsSFBcUERsXFhceHBsgKEIrKCUlKFE6PTBC
     *
     * @param msg 中所带参数
     */
    private void handleUploadUserPhoto(TransmissionMessageTemplate msg) {
       /* String body = msg.getBody();
        String[] bodys = body.split("\tContent=");
        if (bodys.length > 1) {
            String url = fastDfsUtil.uploadFile(bodys[1], "jpg");
            Map<String, String> params = UrlUtil.handleSeparate(bodys[0], "\t", "=");
            laborSystem.updateUserPic(params.get("USERPIC PIN"), params.get("FileName"), url);
        }*/
    }


    /**
     * 上传比对照片
     * 注意: 实际中上传的对比照片是添加人员时的录入人脸信息,不是人脸头像信息
     *
     * @param msg
     */
    private void handleUploadContrastPhoto(TransmissionMessageTemplate msg) {
       /* String body = msg.getBody();
        String[] bodys = body.split("\tContent=");
        if (bodys.length > 1) {
            String url = fastDfsUtil.uploadFile(bodys[1], "jpg");
            Map<String, String> personPicData = UrlUtil.handleSeparate(bodys[0], "\t", "=");

            laborSystem.savePersonPicData(getUrlKey(msg, "SN"), personPicData.get("BIOPHOTO PIN"), url, bodys[1]);
        }*/
    }

    /**
     * 获取设备sn
     *
     * @param msg
     * @return
     */
    public String getUrlKey(TransmissionMessageTemplate msg, String key) {
        String url = msg.getUrl();
        String[] path = url.split("\\?");
        if (path.length > 1) {
            Map<String, String> map = UrlUtil.handleSeparate(path[1], "&", "=");
            return map.get(key);
        } else {
            return "";
        }
    }


}

