package com.xingyun.equipment.admin.modular.device.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Engine;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.device.model.ProjectCrane;
import com.xingyun.equipment.admin.modular.device.service.ProjectCraneService;
import com.xingyun.equipment.admin.modular.projectmanagement.model.ProjectInfo;
import com.xingyun.equipment.admin.modular.projectmanagement.service.IProjectInfoService;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:40 2019/6/27
 * Modified By : wangyifei
 */
@RestController
public class WordAPI {
    @Autowired
    private ProjectCraneService craneService;
    @Autowired
    private IProjectInfoService projectInfoService;

    @GetMapping("ssdevice/word/download")
    public void downloadWord(HttpServletResponse response,  String deviceNo){
        String[] fc = {"小车变幅传感器","高度检测传感器","吊重检测传感器","现场风俗传感器","塔吊回转传感器","继电器输出控制","防倾翻传感器"};
        String fcok = "0000000";
        String rcok = "0000";
        String[] rc = {"IC卡","指纹识别","人脸识别","虹膜识别"};
          if(StrUtil.isBlank(deviceNo)){
              return;
          }
        Wrapper wrapper = new EntityWrapper();
          wrapper.eq("is_del",0);
        wrapper.eq("device_no",deviceNo);
        ProjectCrane crane = craneService.selectOne(wrapper);
        if(crane==null){
            return ;
        }
        ProjectInfo  projectInfo = projectInfoService.selectById(crane.getProjectId());

        Engine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("word.flt");
        StringBuilder peizhi =new StringBuilder();
        StringBuilder shibie = new StringBuilder() ;
        String functionConfig = crane.getFunctionConfig();
        String recognitionConfig = crane.getRecognitionConfig();
        if(fcok.equals(functionConfig)||functionConfig==null){
            peizhi.append("无");
        }else{
            peizhi.append(joinConfig(fc,functionConfig));
        }
        if(rcok.equals(recognitionConfig)||recognitionConfig==null){
            shibie.append("无");
        }else{
            shibie.append(joinConfig(rc,recognitionConfig));
        }
        response.setContentType("application/msword");
        response.setHeader("Content-disposition","filename="+deviceNo+".doc");
        try {
            template.render(Dict.create()
                            .set("beian", emptyReplace(crane.getIdentifier()))
                            .set("gongdi", "无")
                            .set("riqi", emptyReplace(DateUtil.format(crane.getProductionDate(),"yyyy-MM-dd")))
                            .set("xinghao", emptyReplace(crane.getSpecification()))
                            .set("bianhao", "无")
                            .set("changjia", emptyReplace(crane.getManufactor()))
                            .set("danwei", emptyReplace(crane.getOwner()))
                            .set("gongcheng", emptyReplace(projectInfo.getName()))
                            .set("sn", emptyReplace(crane.getCraneNo()))
                            .set("heixiazibianhao", emptyReplace(crane.getDeviceNo()))
                            .set("shouji", emptyReplace(crane.getGprs()))
                            .set("anzhuang", emptyReplace(DateUtil.format(crane.getAssembleDate(),"yyyy-MM-dd")))
                            .set("heixiazixinghao",emptyReplace( crane.getModel()))
                            .set("heixiazichangjia", emptyReplace(crane.getDeviceManufactor()))
                            .set("peizhi", peizhi.toString())
                            .set("shibie", shibie.toString()),

                    response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String emptyReplace(String resource){
        if(StrUtil.isBlank(resource)){
            return "无";
        }else{
            return resource;
        }
    }

    private String joinConfig(String[] configs,String status){
        StringBuilder sb  =new StringBuilder();
        for(int i = 0;i<status.length();i++){
            String s = StrUtil.sub(status,i,i+1);
            if("1".equals(s)){
                sb.append(",");
                sb.append(configs[i]);
            }
        }
        return sb.substring(1);
    }

}
