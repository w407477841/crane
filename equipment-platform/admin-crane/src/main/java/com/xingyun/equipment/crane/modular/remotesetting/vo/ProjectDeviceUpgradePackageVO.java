package com.xingyun.equipment.crane.modular.remotesetting.vo;

import com.xingyun.equipment.crane.modular.remotesetting.model.ProjectDeviceUpgradePackage;
import lombok.Data;

/**
 * @Description:
 * @Author xieshuaishuai
 * @Date Create in 2018/10/8 13:34
 */
@Data
public class ProjectDeviceUpgradePackageVO extends ProjectDeviceUpgradePackage{
    private String createUserName;
    private String typeName;
    private Integer useType;
}
