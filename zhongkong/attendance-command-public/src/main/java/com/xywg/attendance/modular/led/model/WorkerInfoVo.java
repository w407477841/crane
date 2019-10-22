package com.xywg.attendance.modular.led.model;

import lombok.Data;

/**
 * @author hjy
 * @date 2019/5/16
 */
@Data
public class WorkerInfoVo {
    private String classNo;
    private String mobile;
    private String photo;
    private String projectName;
    private String userName;
    private String userId;
    private String workKindName;

    /**
     * 应对接要求  所有null 都返回空串  故全部使用String
      */
    private String workKind;
    private String projectId;
    private String createDate;



    public WorkerInfoVo(String classNo, String mobile, String photo, String projectName, String userName, String userId, String workKindName) {
        this.classNo = classNo;
        this.mobile = mobile;
        this.photo = photo;
        this.projectName = projectName;
        this.userName = userName;
        this.userId = userId;
        this.workKindName = workKindName;
    }

    public WorkerInfoVo() {
    }


    public WorkerInfoVo(String classNo, String mobile, String photo, String projectName, String userName, String userId, String workKindName, String workKind, String projectId, String createDate) {
        this.classNo = classNo;
        this.mobile = mobile;
        this.photo = photo;
        this.projectName = projectName;
        this.userName = userName;
        this.userId = userId;
        this.workKindName = workKindName;
        this.workKind = workKind;
        this.projectId = projectId;
        this.createDate = createDate;
    }
}
