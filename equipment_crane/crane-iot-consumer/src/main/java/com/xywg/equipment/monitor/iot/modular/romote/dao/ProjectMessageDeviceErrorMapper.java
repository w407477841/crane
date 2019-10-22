package com.xywg.equipment.monitor.iot.modular.romote.dao;

import com.xywg.equipment.monitor.iot.modular.romote.model.ProjectMessageDeviceError;
import com.xywg.equipment.monitor.iot.modular.romote.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 * 设备异常日志短信发送履历
 * @author hjy
 */
public interface ProjectMessageDeviceErrorMapper {

   void insert(ProjectMessageDeviceError projectMessageDeviceError);

}
