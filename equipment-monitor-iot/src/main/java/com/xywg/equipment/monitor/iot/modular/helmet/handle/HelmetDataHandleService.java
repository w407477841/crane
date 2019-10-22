package com.xywg.equipment.monitor.iot.modular.helmet.handle;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.xywg.equipment.monitor.iot.core.annotion.SocketCommand;
import com.xywg.equipment.monitor.iot.modular.helmet.model.OriginalData;
import com.xywg.equipment.monitor.iot.modular.helmet.service.DataHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hjy
 * @date 2018/11/22
 */
@Service
public class HelmetDataHandleService {
    @Autowired
    private DataHandleService dataHandleService;

    public void handleData(String message) {
        OriginalData originalData = JSON.parseObject(message, OriginalData.class);
        //该处理程序由另外一个执行,暂时注释
        //reflectMethod(originalData);
    }


    /**
     * 反射调用
     */
    private void reflectMethod(OriginalData originalData) {
        Class<DataHandleService> clazz = DataHandleService.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            boolean methodHasAnnotation = method.isAnnotationPresent(SocketCommand.class);
            if (methodHasAnnotation) {
                SocketCommand socketCommand = method.getAnnotation(SocketCommand.class);
                int desc = socketCommand.command();
                if (desc == originalData.getCommand()) {
                    try {
                        method.invoke(dataHandleService, originalData.getDataDomain());
                        break;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


}
