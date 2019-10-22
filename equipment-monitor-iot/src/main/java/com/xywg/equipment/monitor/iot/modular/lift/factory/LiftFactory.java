package com.xywg.equipment.monitor.iot.modular.lift.factory;

import cn.hutool.core.date.DateUtil;
import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLift;
import com.xywg.equipment.monitor.iot.modular.lift.model.ProjectLiftDetail;
import net.sf.json.JSONObject;

/**
 * @author : wangyifei
 *  Description
 *  Date: Created in 16:49 2018/8/23
 *  Modified By : wangyifei
 */
public class LiftFactory {
    /**
     * @author: wangyifei
     * @param projectLift 设备实体
     * @param object API获取的数据
     * @return 实时数据明细
     */
    public static ProjectLiftDetail factory(ProjectLift projectLift, JSONObject object){
        ProjectLiftDetail  detail = new ProjectLiftDetail();
        detail.setLiftId(projectLift.getId());
        detail.setDeviceNo(object.getString("DeviceSN"));
        detail.setDeviceTime(DateUtil.parseDateTime(object.getString("DeviceTime")));
        detail.setWeight(object.getDouble("LoadValue"));
        detail.setPeople(object.getInt("NumberOfPeople"));
        detail.setStatus(object.getInt("RunningState"));
        int floorStart = object.getInt("StartFloor");
        detail.setFloorStart(floorStart);
        int endFloor  = object.getInt("EndFloor");
        detail.setFloorEnd(endFloor);
        int floor = endFloor - floorStart;
        detail.setFloor(floor>0?floor:-floor);
        return detail;
    }

}
