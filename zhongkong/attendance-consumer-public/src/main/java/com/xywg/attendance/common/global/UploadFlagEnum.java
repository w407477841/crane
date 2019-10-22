package com.xywg.attendance.common.global;


/**
 * @author hjy
 * @date 2019/2/26
 */
public enum UploadFlagEnum {
    /**
     * 上传操作记录
     */
    OPLOG("OPLOG", "上传操作记录"),
    /**
     * 上传用户信息
     */
    USER("USER", "上传用户信息"),
    /**
     * 上传指纹模板
     */
    FP("FP", "上传指纹模板"),
    /**
     * 上传面部模板
     */
    FACE("FACE", "上传面部模板"),
    /**
     * 上传指静脉模板
     */
    FVEIN("FVEIN", "上传指静脉模板"),
    /**
     * 上传用户照片
     */
    USERPIC("USERPIC", "上传用户照片"),
    /**
     * 上传比对照片
     */
    BIOPHOTO("BIOPHOTO", "上传比对照片"),;

    private String name;

    private String flag;

    UploadFlagEnum( String flag,String name) {
        this.name = name;
        this.flag = flag;
    }


    /**
     * flag 匹配  得到枚举
     *
     * @param flag
     * @return
     */
    public static UploadFlagEnum getUploadFlagEnum(String flag) {
        for (UploadFlagEnum u : UploadFlagEnum.values()) {
            if ((u.flag).equalsIgnoreCase(flag)) {
                return u;
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getflag() {
        return flag;
    }

    public void setflag(String flag) {
        this.flag = flag;
    }
}
