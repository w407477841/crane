package com.xywg.attendance.modular.handler.model;

/**
 * @author hjy
 * @date 2019/4/16
 */
public enum CommandTypeEnum {

    // 1重启设备 2清空考勤记录 3删除所有人员 4手动下发人员信息 5移除个别人员

    REBOOT(1, "重启"),
    CLEAR_ATTENDANCE_RECORD(2, "清空考勤记录"),
    DELETE_ALL_PERSON(3, "删除所有数据"),
    DISPATCH_USER(4, "手动下发人员信息"),
    DELETE_SEVERAL_PERSON(5, "移除个别人员"),;

    private Integer type;
    private String name;


    CommandTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * 根据url 匹配  得到方法名
     *
     * @param type
     * @return
     */
    public static CommandTypeEnum getMethodName(Integer type) {
        for (CommandTypeEnum c : CommandTypeEnum.values()) {
            if (c.type.equals(type)) {
                return c;
            }
        }
        return null;
    }


}
