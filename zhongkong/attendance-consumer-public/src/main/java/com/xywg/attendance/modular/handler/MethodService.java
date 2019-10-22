package com.xywg.attendance.modular.handler;

import com.xywg.attendance.common.global.RequestUrlEnum;
import com.xywg.attendance.common.model.TransmissionMessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hjy
 * @date 2019/2/21
 * 所有的设备上报处理
 */
@Service
public class MethodService {

    @Autowired
    private MethodServiceHandle methodServiceHandle;
    private Logger logger = LoggerFactory.getLogger("");


    /**
     * @return
     */
    public void runMethod(TransmissionMessageTemplate msg) {

        String path = msg.getUrl();
        RequestUrlEnum methodNameEnum = RequestUrlEnum.getMethodName(path, msg.getMethod());
        if (methodNameEnum == null) {
            logger.info("There is no match to the associated URL regular expression: " + path);
            return;
        }
        logger.info("Function Description: " + methodNameEnum.getName() + "\tRequest Method: "
                + msg.getMethod()+" \tReceipt Time:"+msg.getDate() + " \tRequest Path: " + msg.getUrl());


       /* if (!methodNameEnum.equals(HANDLE_GET_COMMAND)) {
            //存储有效的原始数据到mongodb,心跳数据不存储
            mongoTemplate.save(msg, MONGODB_ORIGINAL_DATA);
        }*/

        switch (methodNameEnum) {
            //上传更新信息
            case UPLOAD_UPDATE_INFORMATION:
                methodServiceHandle.uploadUpdateInformation(msg);
                break;
            //上传考勤记录
            case ATTENDANCE_RECORD:
                methodServiceHandle.handleUploadAttendance(msg);
                break;
            //上传考勤图片
            case ATTENDANCE_RECORD_PICTURE:
                methodServiceHandle.handleUploadAttendancePhoto(msg);
                break;
            //处理具有相同urL: /iclock/cdata?SN=${SerialNumber}&table=OPERLOG&ContentType=${Value} HTTP/1.1
            case TABLE_OPERLOG:
                //methodServiceHandle.handleTableOperLog(msg);
                break;
            //获取命令
            case HANDLE_GET_COMMAND:
                //methodServiceHandle.handleGetCommand(msg);
                break;
            //命令回复
            case HANDLE_COMMAND_RESPONSE:
                methodServiceHandle.handleCommandResponse(msg);
                break;
            default:
                break;
        }
    }

}
//            //上传身份证信息
//            case UPLOAD_IDCARD_INFORMATION:
//                methodServiceHandle.uploadIDCardInformation( msg);
//                break;
//              //异地考勤
//            case "handleBeyondAttendance":
//                methodServiceHandle.handleBeyondAttendance(msg);
//                break;
//            //上传一体化模板
//            case "handleUploadTempPhoto":
//                methodServiceHandle.handleUploadTempPhoto(msg);
//                break;

//            //交换公钥（支持通信加密的场合）
//            case "handleExchangePublicKey":
//                methodServiceHandle.handleExchangePublicKey( msg);
//                break;
//            //交换因子（支持通信加密的场合）
//            case "handleExchangeFactor":
//                methodServiceHandle.handleExchangeFactor( msg);
//                break;
//            //推送配置信息
//            case "pushConfigInformation":
//                methodServiceHandle.pushConfigInformation(msg);
//                break;