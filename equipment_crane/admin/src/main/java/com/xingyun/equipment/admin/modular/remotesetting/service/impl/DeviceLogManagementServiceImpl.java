package com.xingyun.equipment.admin.modular.remotesetting.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.core.util.HttpUtils;
import com.xingyun.equipment.admin.modular.remotesetting.dao.DeviceLogManagementMapper;
import com.xingyun.equipment.admin.modular.remotesetting.model.DeviceLogInfo;
import com.xingyun.equipment.admin.modular.remotesetting.model.ProjectDeviceRestartRecord;
import com.xingyun.equipment.admin.modular.remotesetting.service.DeviceLogManagementService;
import com.xingyun.equipment.admin.modular.remotesetting.service.ProjectDeviceRestartRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjy
 * @since 2018-09-30
 */
@Service
public class DeviceLogManagementServiceImpl implements DeviceLogManagementService {
    @Value("${iot.url}")
    String iotUrl;
    @Autowired
    private DeviceLogManagementMapper deviceLogManagementMapper;
    @Autowired
    private ProjectDeviceRestartRecordService projectDeviceRestartRecordService;

    @Override
    public ResultDTO<DataDTO<List<DeviceLogInfo>>> getPageList(RequestDTO<DeviceLogInfo> request) {
        Page<DeviceLogInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        DeviceLogInfo deviceLogInfo = new DeviceLogInfo();
        deviceLogInfo.setDeviceType(request.getType());
        deviceLogInfo.setKey(request.getKey());
        List<DeviceLogInfo> list = deviceLogManagementMapper.getPageList(page, deviceLogInfo);
        return new ResultDTO<>(true, DataDTO.factory(list, page.getTotal()));
    }

    @Override
    public ResultDTO<Object> deviceReboot(RequestDTO<List<DeviceLogInfo>> request) {
        List<DeviceLogInfo> list = request.getBody();
        if (list == null) {
            return new ResultDTO<>(true);
        }
        StringBuilder deviceNoListBuilder = new StringBuilder();
        for (DeviceLogInfo deviceLogInfo : list) {
            //这里暂时只会有扬尘设备
            deviceNoListBuilder.append(deviceLogInfo.getDeviceNo()).append(",");
        }

        Map<String, String> paramMap = new HashMap<>(1);
        paramMap.put("deviceNoList", deviceNoListBuilder.toString());
        String data = JSONObject.toJSONString(paramMap);
        String url = "http://" + iotUrl + "/ssdevice/activeIssued/rebootDevice";
        //通知设备
        String res = HttpUtils.sendPost(url, data);
        String flag = "error";

        StringBuilder resStringBuilder = new StringBuilder();

        List<ProjectDeviceRestartRecord> listDeviceRestartRecord = new ArrayList<>();
        if (!flag.equals(res)) {
            //成功发送了重启指令的设备
            String[] deviceNoList = res.split(",");
            for (DeviceLogInfo deviceLogInfo : list) {
                boolean f = Arrays.asList(deviceNoList).contains(deviceLogInfo.getDeviceNo());
                if (f) {
                    ProjectDeviceRestartRecord record = new ProjectDeviceRestartRecord(
                            deviceLogInfo.getDeviceNo(),
                            deviceLogInfo.getDeviceType(),
                            deviceLogInfo.getProjectId(),
                            new Date()
                    );
                    listDeviceRestartRecord.add(record);
                } else {
                    //没有发送成功的
                    resStringBuilder.append(deviceLogInfo.getDeviceNo()).append(",");
                }
            }
            if(listDeviceRestartRecord.size()>0){
                //保存已经发送成了的
                projectDeviceRestartRecordService.insertBatch(listDeviceRestartRecord);
            }
            //构建返回信息
            if (StringUtils.isNotBlank(resStringBuilder.toString())) {
                String resString = resStringBuilder.toString().substring(0, resStringBuilder.toString().length() - 1);
                return new ResultDTO<>(true, null,
                        "操作成功,"+resString + "设备不在线,无法下发重启指令");
            } else {
                return new ResultDTO<>(true, null, "操作成功");
            }
        } else {
            return new ResultDTO<>(false, null, "网络错误,无法下发重启指令");
        }
    }
}
