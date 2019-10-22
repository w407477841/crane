package com.xingyun.equipment.admin.modular.alipay.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xingyun.equipment.admin.config.AliPayConfig;
import com.xingyun.equipment.admin.core.dto.DataDTO;
import com.xingyun.equipment.admin.core.dto.RequestDTO;
import com.xingyun.equipment.admin.core.dto.ResultDTO;
import com.xingyun.equipment.admin.modular.alipay.model.ProjectTopUp;
import com.xingyun.equipment.admin.modular.alipay.model.ProjectTopUpDTO;
import com.xingyun.equipment.admin.modular.alipay.service.IProjectTopUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 塔吊充值控制器
 *
 * @author fengshuonan
 * @Date 2019-06-25 14:15:59
 */
@RestController
@RequestMapping("/ssdevice/ProjectTopUp")
public class ProjectTopUpController {



    @Autowired(required = true)
    private IProjectTopUpService ProjectTopUpService;



    /**
     * 获取塔吊充值列表
     */
    @PostMapping(value = "/list")
    public ResultDTO<DataDTO<List<ProjectTopUp>>> list(@RequestBody RequestDTO<ProjectTopUp> request) {
        return ProjectTopUpService.getPageList(request);
    }

    /**
     * 新增塔吊充值
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public ResultDTO<Object> add(ProjectTopUp ProjectTopUp) {
        ProjectTopUpService.insert(ProjectTopUp);
        return new ResultDTO<>(true);
    }

    /**
     * 删除塔吊充值
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public ResultDTO<Object> delete(@RequestParam Integer id) {
        ProjectTopUpService.deleteById(id);
        return new ResultDTO<>(true);
    }

    /**
     * 修改塔吊充值
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public ResultDTO<DataDTO<List<ProjectTopUp>>> update(ProjectTopUp ProjectTopUp) {
        ProjectTopUpService.updateById(ProjectTopUp);
        return new ResultDTO<>(true);
    }

    /**
     * 塔吊充值详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail(Integer id) {

        return ProjectTopUpService.selectById(id);
    }

    /**
     * 处理订单（商务部）
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateStatus")
    @ResponseBody
    public ResultDTO<ProjectTopUp> updateStatus(Integer id) {
        EntityWrapper<ProjectTopUp> wrapper = new EntityWrapper<>();
        wrapper.eq("id",id);

        ProjectTopUp topUp = ProjectTopUpService.selectOne(wrapper);
       boolean flag= updateSim(topUp.getGprs());
       if(flag){
           topUp.setStatus(1);

            ProjectTopUpService.update(topUp,wrapper);
            return  new ResultDTO<ProjectTopUp>(true);
       }else{
           return  new ResultDTO<ProjectTopUp>(true,null,"无法激活,查看SIM卡号是否正确");
       }

    }

    /**
     * 更新sim卡状态
     * @param sim
     * @return
     */
    public boolean updateSim(String sim){

//        String string="yinyanqin3222;e26f0373-7000-4c64-a2fb-71d5025a25fc";
//
//        String base64Sign = Base64.encode(string.getBytes());
//        System.out.println(base64Sign);
        String url="https://api.10646.cn/rws/api/v1/devices/"+sim;
        JSONObject json =new JSONObject();
        json.put("status","ACTIVATED");
        System.out.println(json);
        HttpResponse res= HttpRequest.put(url)
              .header("Authorization",AliPayConfig.SIM_BASE64)
                .body(json)
              .execute();
        if(res.getStatus()==200){
            return true;
        }else{
            return false;
        }

    }
}
