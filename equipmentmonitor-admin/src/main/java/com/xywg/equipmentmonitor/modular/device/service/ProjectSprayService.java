package com.xywg.equipmentmonitor.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectSpray;
import com.xywg.equipmentmonitor.modular.device.vo.ProjectSprayVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description:
 * @Author xiess
 * @Date Create in 2019/4/2 10:54
 */
@Service
public interface ProjectSprayService extends IService<ProjectSpray> {

    ResultDTO<DataDTO<List<ProjectSprayVO>>> getPageList(RequestDTO<ProjectSprayVO> request);

    ResultDTO<ProjectSprayVO> selectInfo(RequestDTO<ProjectSprayVO> request);

    ResultDTO<ProjectSprayVO> checkByDeviceNo(RequestDTO<ProjectSprayVO> request);

    ResultDTO<ProjectSpray> deviceReboot( RequestDTO<List<ProjectSpray>> request);

    ResultDTO<ProjectSpray> deviceOpenClose( RequestDTO<List<ProjectSpray>> request);

    ResultDTO<List<ProjectSpray>> getSpraysByEnvironDevice(String deviceNo);

}
