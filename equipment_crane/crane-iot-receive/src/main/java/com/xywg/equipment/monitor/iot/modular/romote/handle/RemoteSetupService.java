package com.xywg.equipment.monitor.iot.modular.romote.handle;

import com.xywg.equipment.monitor.iot.config.properties.XywgProerties;
import com.xywg.equipment.monitor.iot.core.util.DateUtils;
import com.xingyun.crane.cache.RedisUtil;;
import com.xywg.equipment.monitor.iot.core.util.SMSUtils;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCrane;
import com.xywg.equipment.monitor.iot.modular.crane.service.IProjectCraneService;
import com.xywg.equipment.monitor.iot.modular.romote.dao.ProjectDeviceErrorLogMapper;
import com.xywg.equipment.monitor.iot.modular.romote.dao.ProjectMessageDeviceErrorMapper;
import com.xywg.equipment.monitor.iot.modular.romote.dao.ProjectMessageUserDeviceErrorLogMapper;
import com.xywg.equipment.monitor.iot.modular.romote.dao.UserMapper;
import com.xywg.equipment.monitor.iot.modular.romote.model.ProjectDeviceErrorLog;
import com.xywg.equipment.monitor.iot.modular.romote.model.ProjectMessageDeviceError;
import com.xywg.equipment.monitor.iot.modular.romote.model.ProjectMessageUserDeviceErrorLog;
import com.xywg.equipment.monitor.iot.modular.romote.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author hjy
 * @date 2018/10/16
 * 远程控制(包含了设备异常时日志上报的业务处理,短信通知)
 */
@Service
@SuppressWarnings("all")
public class RemoteSetupService {

    @Autowired
    private ProjectDeviceErrorLogMapper projectDeviceErrorLogMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    protected XywgProerties xywgProerties;
    @Autowired
    private ProjectMessageUserDeviceErrorLogMapper projectMessageUserDeviceErrorLogMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProjectMessageDeviceErrorMapper projectMessageDeviceErrorMapper;
    @Autowired
    private IProjectCraneService projectCraneService;


    private Logger logger = LoggerFactory.getLogger(RemoteSetupService.class);



    /**
     * 塔吊 异常情况
     * @param sn
     * @param exceptionMessageType
     * @param deviceType
     */
    @Transactional(rollbackFor = Exception.class)
    public void deviceCraneExceptionalLog(String sn, String exceptionMessageType, int deviceType) {
        // 获取 设备信息
        ProjectCrane crane = projectCraneService.selectOne(sn);
        //所属项目id
        Integer projectId = crane.getProjectId();
        String msg = "";
        for(int i=0;i<8;i++){
            if("0".equals(exceptionMessageType.substring(i,i+1))){
                continue;
            }
            String exceptionMessage = deviceCraneExceptionalType(""+(i+1));
            if(StringUtils.isBlank(exceptionMessage)){
                continue;
            }else{
                if(StringUtils.isBlank(msg)){
                    msg+= exceptionMessage;
                }else{
                    msg+=(","+ exceptionMessage);
                }
            }

        }
        if(StringUtils.isBlank(msg)){
            return ;
        }
        ProjectDeviceErrorLog projectDeviceErrorLog = new ProjectDeviceErrorLog(sn, msg, deviceType, projectId, new Date(), null);
        projectDeviceErrorLogMapper.insert(projectDeviceErrorLog);
        String smsMessage = "塔吊设备:" + sn + msg;
        pushLogic(sn,smsMessage,deviceType,projectId);
    }


    /**
     * 短信发送逻辑处理
     *
     * @param sn        设备号
     * @param phone     用户手机号,多个以逗号隔开
     * @param ids       发送人ids
     * @param projectId 项目id
     * @param smsMessage    信息内容
     */
    private void smsSend(String sn, String ids, String phone, Integer projectId,String smsMessage) {
        try {
            //真正的发送短信
            SMSUtils.sendSMSMessage(phone, smsMessage);

            ProjectMessageDeviceError projectMessageDeviceError = new ProjectMessageDeviceError();
            projectMessageDeviceError.setContent(smsMessage);
            projectMessageDeviceError.setDeviceNo(sn);
            projectMessageDeviceError.setProjectId(projectId);
            projectMessageDeviceError.setUserIds(ids);
            //保存发送日志
            projectMessageDeviceErrorMapper.insert(projectMessageDeviceError);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("短信发送失败:" + DateUtils.getDateTime() + " 发送人:" + phone + " 内容:" + smsMessage);
        }
    }



    /**
     * 塔吊
     * @param exceptionalType 异常数据类型
     * @return
     */
    private String deviceCraneExceptionalType(String exceptionalType) {
        String exceptionalTypeMessage = "";
        switch (exceptionalType) {
            case "1":
                exceptionalTypeMessage = "未检测到SD卡";
                //exceptionalTypeMessage += ",TF卡未接入或损坏";
                break;
            case "2":
                exceptionalTypeMessage = "EEPROM错误";
                //exceptionalTypeMessage += ",风速传感器可能发生异常";
                break;
            case "3":
                exceptionalTypeMessage = "未检测到风速传感器";
               // exceptionalTypeMessage += ",风向传感器可能发生异常";
                break;
            case "4":
                exceptionalTypeMessage = "未检测到回转传感器";
                //exceptionalTypeMessage += ",小型气象站可能发生异常";
                break;
            case "5":
                exceptionalTypeMessage = "未检测到高度传感器";
                //exceptionalTypeMessage += ",小型气象站可能发生异常";
                break;
            case "6":
                exceptionalTypeMessage = "未检测到幅度传感器";
                //exceptionalTypeMessage += ",小型气象站可能发生异常";
                break;
            case "7":
                exceptionalTypeMessage = "未检测到称重传感器";
               // exceptionalTypeMessage += ",小型气象站可能发生异常";
                break;
            case "8":
                exceptionalTypeMessage = "未检测到倾角传感器";
                break;
            default:
                break;
        }
        return exceptionalTypeMessage;
    }

    /**
     * 短信推送逻辑处理
     * @param sn
     * @param smsMessage
     * @param deviceType
     * @param projectId
     */
    public void pushLogic(String sn,String smsMessage ,Integer deviceType,Integer projectId){
        String key = xywgProerties.getRedisHead() + ":deviceExceptionalLog_" + sn;
        Object numberObject = redisUtil.hmGet(key, smsMessage);
        //次数
        Integer number = Integer.parseInt(numberObject == null ? "0" : numberObject.toString());
        //有效时间10分钟
        redisUtil.hmSet(key, smsMessage, number + 1, 10L);

        int count = 3;
        //第一次发送短信:第三次异常数据   后面发送短信:每10次推送一次
        if ((number + 1) == count || (number + 1) % 10 == 0) {
            ProjectMessageUserDeviceErrorLog userDeviceErrorLog = projectMessageUserDeviceErrorLogMapper.select(deviceType);
            if (userDeviceErrorLog != null && StringUtils.isNotBlank(userDeviceErrorLog.getUserIds())) {
                String[] ids = userDeviceErrorLog.getUserIds().split(",");
                //查询短信推送用户
                if (ids.length > 0) {
                    List<User> userList = userMapper.getListUserByIds(ids);
                    StringBuilder phone = new StringBuilder();
                    for (User user : userList) {
                        phone.append(user.getPhone()).append(",");
                    }
                    //String smsMessage = "扬尘设备:" + sn + sensor;
                    smsSend(sn, userDeviceErrorLog.getUserIds(), phone.toString(), projectId, smsMessage);
                }
            }
        }
    }
}
