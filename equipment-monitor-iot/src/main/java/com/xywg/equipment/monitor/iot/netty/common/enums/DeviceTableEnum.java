package com.xywg.equipment.monitor.iot.netty.common.enums;

/**
 * 所有设备对应表
 *
 * @author hjy
 */
public enum DeviceTableEnum {

    /**
     * 扬尘
     */
    DUST("02", "t_project_environment_monitor"),
    /**
     * 塔吊
     */
    CRANE("03", "t_project_crane"),
    /**
     * 喷淋
     */
    SPRAY("0F","t_project_spray");

    private String deviceTypeCode;

    private String deviceTypeTableName;

    DeviceTableEnum(String deviceTypeCode, String deviceTypeTableName) {
        this.deviceTypeCode = deviceTypeCode;
        this.deviceTypeTableName = deviceTypeTableName;
    }

    /**
     * 根据code 获取表名
     * @param deviceTypeCode
     * @return
     */
    public static String getDeviceTypeTableName(String deviceTypeCode) {
        for (DeviceTableEnum c : DeviceTableEnum.values()) {
            if (c.getDeviceTypeCode().equals(deviceTypeCode)) {
                return c.deviceTypeTableName;
            }
        }
        return null;
    }

    public String getDeviceTypeCode() {
        return deviceTypeCode;
    }

    public void setDeviceTypeCode(String deviceTypeCode) {
        this.deviceTypeCode = deviceTypeCode;
    }

    public String getDeviceTypeTableName() {
        return deviceTypeTableName;
    }

    public void setDeviceTypeTableName(String deviceTypeTableName) {
        this.deviceTypeTableName = deviceTypeTableName;
    }
}
