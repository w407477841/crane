package com.xywg.equipmentmonitor.modular.device.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.equipmentmonitor.core.common.constant.OperationEnum;
import com.xywg.equipmentmonitor.core.controller.BaseController;
import com.xywg.equipmentmonitor.core.dto.DataDTO;
import com.xywg.equipmentmonitor.core.dto.RequestDTO;
import com.xywg.equipmentmonitor.core.dto.ResultDTO;
import com.xywg.equipmentmonitor.modular.device.dto.ProjectTransfersDTO;
import com.xywg.equipmentmonitor.modular.device.model.ProjectCrane;
import com.xywg.equipmentmonitor.modular.device.model.ProjectEnvironmentMonitor;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmet;
import com.xywg.equipmentmonitor.modular.device.model.ProjectHelmetAlarm;
import com.xywg.equipmentmonitor.modular.device.service.IProjectHelmetAlarmService;
import com.xywg.equipmentmonitor.modular.device.service.IProjectHelmetService;
import com.xywg.equipmentmonitor.modular.station.model.ProjectDeviceStock;
import com.xywg.equipmentmonitor.modular.station.service.impl.ProjectDeviceStockServiceImpl;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 安全帽
 *
 * @author : HJY
 */
@RestController
@RequestMapping("ssdevice/device/helmet")
public class ProjectHelmetController extends BaseController<ProjectHelmet, IProjectHelmetService> {

    @Autowired
    private IProjectHelmetAlarmService helmetAlarmService;

    @Autowired
    private ProjectDeviceStockServiceImpl projectDeviceStockService;

    /**
     * 安全帽一览
     *
     * @param res
     * @return
     */
    @ApiOperation("安全帽一览")
    @PostMapping("selectPageList")
    ResultDTO<DataDTO<List<ProjectHelmet>>> selectPageList(@RequestBody RequestDTO<ProjectHelmet> res) {
        return service.selectPageList(res);
    }

    /**
     * 启用
     *
     * @param res
     * @return
     */
    @ApiOperation("启用")
    @PostMapping("updateStatus")
    public ResultDTO<Object> updateStatus(@RequestBody RequestDTO<ProjectHelmet> res) {
        return service.updateStatus(res);
    }

    /**
     * 项目调度
     *
     * @param res
     * @return
     */
    @ApiOperation("项目调度")
    @PostMapping("projectTransfers")
    public ResultDTO<Object> projectTransfers(@RequestBody RequestDTO<ProjectTransfersDTO> res) {
        return service.projectTransfers(res);
    }


    /**
     * 人员调度
     *
     * @param res
     * @return
     */
    @ApiOperation("人员调度")
    @PostMapping("personTransfers")
    public ResultDTO<Object> personTransfers(@RequestBody RequestDTO<ProjectHelmet> res) {
        return service.personTransfers(res);
    }

    /**
     * 单条
     *
     * @param res
     * @return
     * @author yuanyang
     */
    @ApiOperation("单条详情")
    @PostMapping("selectInfo")
    public ResultDTO<ProjectHelmet> selectInfo(@RequestBody RequestDTO<ProjectHelmet> res) {
        return new ResultDTO<>(true, service.selectById(res.getId()));
    }

    /**
     * 新增或编辑
     * @param res
     * @return
     */
    @PostMapping("updateOrInsertProjectHelmet")
    @ApiOperation("新增或编辑")
    ResultDTO<Object> updateOrInsertProjectHelmet(@RequestBody RequestDTO<ProjectHelmet> res){
        return service.updateOrInsertProjectHelmet(res);
    }

    @GetMapping("getAlarmMessage")
    @ApiOperation("按报警类型统计")
    public ResultDTO<DataDTO<List<ProjectHelmetAlarm>>> getAlarmMessage(RequestDTO request){
        return helmetAlarmService.getAlarmMessage(request);
    }

    @GetMapping("getListByHelmetId")
    @ApiOperation("查询报警详情信息")
    public ResultDTO<DataDTO<List<ProjectHelmetAlarm>>> getListByHelmetId(RequestDTO request){
        return helmetAlarmService.getListByHelmetId(request);
    }


    @PostMapping("deletes")
    @Override
    public ResultDTO<ProjectHelmet>  deletes(@RequestBody  RequestDTO<ProjectHelmet> t){
        hasPermission(deleteRole());
        try {
            List<ProjectDeviceStock> list= new ArrayList<>();
            for(int i=0;i<t.getIds().size();i++){
                Integer id= (Integer) t.getIds().get(i);
                EntityWrapper<ProjectHelmet> wrapper =new EntityWrapper<>();
                wrapper.eq("id",id);
                ProjectHelmet helmet=  service.selectOne(wrapper);
                if(helmet !=null){
                    Map<String, Object> map = new HashMap<>();
                    map.put("device_no",helmet.getImei());
                    map.put("is_del",0);
                    List<ProjectDeviceStock> stocks= projectDeviceStockService.selectByMap(map);
                    if(stocks.size()>0){
                        ProjectDeviceStock stock = stocks.get(0);
                        stock.setStatus(0);
                        list.add(stock);
                    }
                }



            }
            if(service.deleteBatchIds(t.getIds())){
                if(list.size()>0){
                    projectDeviceStockService.updateAllColumnBatchById(list);
                    return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.resultFactory(OperationEnum.DELETE_ERROR);
        }
        return ResultDTO.resultFactory(OperationEnum.DELETE_SUCCESS);
    }


    @PostMapping("bindWechat")
    public ResultDTO<Object>  bindWechat(@RequestBody  RequestDTO<ProjectHelmet> t){
        return  service.bindWeChat(t);
    }

    @PostMapping("updateWechat")
    public ResultDTO<Object>  updateWechat(@RequestBody  RequestDTO<ProjectHelmet> t){
        return  service.updateWechat(t);
    }

    @GetMapping("getwechatInfo")
    public ResultDTO<Object>  getwechatInfo(String jsCode){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("appid","wxd1d1cdafe09ccb0a");
        paramMap.put("secret","bb7ad4466499b56d46af382ca28ee860");
        paramMap.put("js_code",jsCode);
        paramMap.put("grant_type","authorization_code");
        paramMap.put("connect_redirect","1");
       String str= HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session",paramMap);
       JSONObject json = JSONObject.parseObject(str);
       if(json.get("errcode") !=null){
           return  new ResultDTO<>(false,null,json.get("errmsg").toString());
       }else{
           Map<String,Object> map = new HashMap<>();
           map.put("openid",json.get("openid"));
           map.put("session_key",json.get("session_key"));
           return  new ResultDTO<>(true,map,"操作成功");
       }



    }



    @Override
    public String insertRole() {
        return null;
    }

    @Override
    public String updateRole() {
        return null;
    }

    @Override
    public String deleteRole() {
        return null;
    }

    @Override
    public String viewRole() {
        return null;
    }


}
