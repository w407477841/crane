package com.xywg.attendance.modular.handler.model;

import lombok.Data;

/**
 * @author hjy
 * @date 2019/4/16
 */
@Data
public class WorkerInfo {

    /**
     * 身份证号码
     */
    private String idCardNumber;
    /**
     * 姓名
     */
    private String name;

    /**
     * 人脸图片地址(大头贴)
     */
    private String userPhotosUrl;
    /**
     * 对比照
     */
    private String faceTemplateUrl;



}
