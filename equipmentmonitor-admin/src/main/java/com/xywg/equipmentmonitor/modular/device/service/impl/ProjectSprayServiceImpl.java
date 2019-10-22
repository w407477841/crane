package com.xywg.equipmentmonitor.modular.device.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.core.util.HttpUtils;
import com.xywg.equipmentmonitor.modular.device.dao.ProjectSprayMapper;
import com.xywg.equipmentmonitor.modular.device.dto.SprayControl;
import com.xywg.equipmentmonitor.modular.device.model.ProjectSpray;
import com.xywg.equipmentmonitor.modular.device.model.ProjectSprayDetail;
import com.xywg.equipmentmonitor.modular.device.service.ProjectSprayDetailService;
import com.xywg.equipmentmonitor.modular.device.service.ProjectSprayService;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectSprayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 10:54
 */
@Service
public class ProjectSprayServiceImpl extends ServiceImpl<ProjectSprayMapper, ProjectSpray> implements ProjectSprayService {
    @Value("${iot.url}")
    String iotUrl;

    @Autowired
    private ProjectSprayDetailService projectSprayDetailService;
    @Autowired
    private ProjectSprayMapper projectSprayMapper;

    @Override
    public ResultDTO<DataDTO<List<ProjectSprayVO>>> getPageList(RequestDTO<ProjectSprayVO> request) {
        Page<ProjectSprayVO> page = new Page<ProjectSprayVO>(request.getPageNum(), request.getPageSize());
        EntityWrapper<RequestDTO> wrapper = new EntityWrapper<>();
        wrapper.eq("s.is_del", 0).in("s.org_id",request.getOrgIds());;
        wrapper.orderBy("s.create_time", false);
        if (request.getProjectId() != null) {
            wrapper.eq("s.project_id", request.getProjectId());
        }
        if(request.getId()!=null && !StrUtil.isEmpty(request.getId().toString())){
            wrapper.eq("s.project_id",request.getId());
        }
        String deviceNo=request.getDeviceNo();
        wrapper.like(StrUtil.isNotBlank(deviceNo),"device_no",deviceNo);
        List<ProjectSprayVO> list = baseMapper.getPageList(page, wrapper);
        ResultDTO<DataDTO<List<ProjectSprayVO>>> result = new ResultDTO(true, DataDTO.factory(list, page.getTotal()));
        return result;
    }

    @Override
    public ResultDTO<ProjectSprayVO> selectInfo(RequestDTO<ProjectSprayVO> request) {
        return new ResultDTO(true, baseMapper.getById(Integer.parseInt(request.getId().toString())));
    }

    @Override
    public ResultDTO<ProjectSprayVO> checkByDeviceNo(RequestDTO<ProjectSprayVO> request) {
        int num = baseMapper.checkByDeviceNo(request);
        return new ResultDTO(true, num);
    }

    @Override
    public ResultDTO<ProjectSpray> deviceReboot(RequestDTO<List<ProjectSpray>> request) {
        List<ProjectSpray> list = request.getBody();
        if (list.size() == 0) {
            return new ResultDTO<>(true, null, "操作成功");
        }
        StringBuilder sb = new StringBuilder();
        List<ProjectSprayDetail> sprayDetailList = new ArrayList<>();
        for (ProjectSpray projectSpray : list) {
            sb.append(projectSpray.getDeviceNo()).append(",");
            ProjectSprayDetail sprayDetail = new ProjectSprayDetail();
            sprayDetail.setSprayId(projectSpray.getId());
            sprayDetail.setDeviceNo(projectSpray.getDeviceNo());
            sprayDetail.setControlMode(1);
            sprayDetail.setOperationType(2);

            sprayDetailList.add(sprayDetail);
        }
        projectSprayDetailService.insertBatch(sprayDetailList);

        String data = JSONObject.toJSONString(sprayDetailList);
        String url = "http://" + iotUrl + "/ssdevice/activeIssued/rebootDeviceSpray";
        //通知设备
        HttpUtils.sendPost(url, data);

        return new ResultDTO<>(true, null, "操作成功");
    }

    @Override
    public ResultDTO<ProjectSpray> deviceOpenClose(RequestDTO<List<ProjectSpray>> request) {
        List<ProjectSpray> list = request.getBody();
        if (list.size() == 0) {
            return new ResultDTO<>(true, null, "操作成功");
        }
        SprayControl sprayControl= new SprayControl();
        sprayControl.setList(list);
        sprayControl.setType(request.getType());

        String data = JSONObject.toJSONString(sprayControl);
        String url = "http://" + iotUrl + "/ssdevice/activeIssued/controlSpray";
        //通知设备
        String res =HttpUtils.sendPost(url, data);
        return new ResultDTO<>(true, null, res);
    }
@Override
    public ResultDTO<List<ProjectSpray>> getSpraysByEnvironDevice(String deviceNo)
    {
        List<ProjectSpray> projectSprayList= projectSprayMapper.getSpraysByEnvironDevice(deviceNo);
        return new ResultDTO<>(true,projectSprayList,null);
    }
}
