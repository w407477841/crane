package com.xywg.equipment.monitor.iot.modular.crane.factory;

import cn.hutool.core.date.DateUtil;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCrane;
import com.xywg.equipment.monitor.iot.modular.crane.model.ProjectCraneDetail;
import net.sf.json.JSONObject;

/**
 * @Author : wangyifei
 * @Description
 * @Date: Created in 15:01 2018/8/23
 * @Modified By : wangyifei
 */
public class CraneFactory {
/**
* @Author: wangyifei
* @Description:
* @Date: 15:03 2018/8/23
*/
    public static ProjectCraneDetail factory( ProjectCrane crane, JSONObject  detail){
        ProjectCraneDetail  projectCraneDetail = new ProjectCraneDetail();
        projectCraneDetail.setCraneId(crane.getId());
        projectCraneDetail.setDeviceNo(detail.getString("DeviceSN"));
        projectCraneDetail.setWeight(detail.getDouble("Load"));
        projectCraneDetail.setRange(detail.getDouble("Radius"));
        projectCraneDetail.setMomentPercentage(detail.optDouble("Percent"));
        projectCraneDetail.setHeight(detail.getDouble("Height"));
        //  SafeLoad 暂无
        projectCraneDetail.setRotaryAngle(detail.getDouble("Angle"));
        projectCraneDetail.setDeviceTime(DateUtil.parseDateTime(detail.getString("InsertTime")));
        projectCraneDetail.setTiltAngle(detail.getDouble("ObliqueAngle"));
        projectCraneDetail.setWindSpeed(detail.getDouble("WindSpeed"));
        return projectCraneDetail;
    }

}
