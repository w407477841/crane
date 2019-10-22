package com.xywg.equipmentmonitor.modular.remotesetting.model;

import com.xywg.equipmentmonitor.core.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author hjy
 * 设备升级
 */
@Data
public class ProjectDeviceUpgrade extends BaseEntity<ProjectDeviceUpgrade> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;

    private String deviceNo;

    private Integer projectId;

    private String projectName;

    private String version;

    private String upgradeTime;

    private String path;

    private Integer flag;

    /**
     * 最新下发升级指令的时间
     */
    private Date commandUpgradeTime;

    /**
     * 固件版本号
     */
    private String firmwareVersion;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public ProjectDeviceUpgrade(String deviceNo, Integer projectId, String projectName, String version, String upgradeTime, String path) {
        this.deviceNo = deviceNo;
        this.projectId = projectId;
        this.projectName = projectName;
        this.version = version;
        this.upgradeTime = upgradeTime;
        this.path = path;
    }

    public ProjectDeviceUpgrade() {
    }
}
