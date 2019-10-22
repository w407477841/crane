package com.xingyun.equipment.admin.modular.device.controller;

import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.service.IShuiDian2ComService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;

/**
 * @author : wangyifei
 * Description  水电管理 -  给企业版的数据接口
 * Date: Created in 10:47 2018/10/22
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("ssdevice/shuidian")
public class ShuiDian2ComController {

    @Autowired
    IShuiDian2ComService   shuiDian2ComService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShuiDian2ComController.class);
    /**
    * @author: wangyifei
    * Description:  获取当前水电表 正常数和停用数
     * @param uuid  项目UUID
    * Date: 10:52 2018/10/22
    */
    @GetMapping("total")
    public ResultDTO getShuiDianTotal(@RequestParam(value="uuid") String uuid){
        return shuiDian2ComService.shuiDianTotal(uuid);
    }
    /**
    * @author: wangyifei
    * Description: 获取当月有报警的 水电表数
    * Date: 14:12 2018/10/22
    */
    @GetMapping("exceptions")
    public ResultDTO  getShuiDianEx(@RequestParam(value="uuid") String uuid){


        return shuiDian2ComService.shuiDianEx(uuid);
    }

    /**
     * 7日用水量
     * @param uuid
     * @return
     */
    @GetMapping("waterTotal")
    public ResultDTO  getWaterTotal(@RequestParam(value = "uuid")String uuid){

        return shuiDian2ComService.getWaterTotal(uuid);
    }

    /**
     * 7日用电量
     * @param uuid
     * @return
     */
    @GetMapping("powerTotal")
    public ResultDTO  getPowerTotal(@RequestParam(value = "uuid")String uuid){

        return shuiDian2ComService.getPowerTotal(uuid);
    }



    /**
    * @author: wangyifei
    * Description: 水量七日折线图
    * Date: 8:55 2018/10/23
    */
@GetMapping("waterLineChart")
    public ResultDTO getWaterLineCharts(@RequestParam(value = "uuid")String uuid,@RequestParam(value="type",required = false) Integer  type ){
        return shuiDian2ComService.getWaterLineCharts(uuid,type);
    }

    /**
     * @author: wangyifei
     * Description: 电量七日折线图
     * Date: 8:55 2018/10/23
     */
    @GetMapping("powerLineChart")
    public ResultDTO getPowerLineCharts(@RequestParam(value = "uuid")String uuid,@RequestParam(value="type",required = false) Integer  type ){
        return shuiDian2ComService.getPowerLineCharts(uuid,type);
    }



}
