package com.xingyun.equipment.crane.modular.device.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xingyun.equipment.Const;
import com.xingyun.equipment.core.dto.DataDTO;
import com.xingyun.equipment.core.dto.RequestDTO;
import com.xingyun.equipment.core.dto.ResultDTO;
import com.xingyun.equipment.crane.core.util.SMSUtils;
import com.xingyun.equipment.crane.modular.device.vo.RealTimeMonitoringTowerVo;
import com.xingyun.equipment.crane.modular.device.vo.SendMessageDTO;
import com.xingyun.equipment.system.model.User;
import com.xingyun.equipment.system.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:27 2019/6/26
 * Modified By : wangyifei
 */
@RestController
public class MessageController {
    @Autowired
    private IUserService userService;

    @PostMapping(value="admin-crane/message/sendMessage")
    public ResultDTO sendMessage(@RequestBody RequestDTO<SendMessageDTO> requestDTO){
        String content = requestDTO.getBody().getContent();
        String phone = requestDTO.getBody().getPhone();
        if(StrUtil.isBlank(content)){
            return new ResultDTO(false,null,"内容不能空");
        }
        if(StrUtil.isBlank(phone)){
            return new ResultDTO(false,null,"手机不能空");
        }
        try {
            SMSUtils.sendSMSMessage(phone,content);
            return new ResultDTO(true,null,"短信发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultDTO(false,null,"短信发送失败");
        }
    }
    @PostMapping("admin-crane/message/userList")
    public ResultDTO userList(@RequestBody RequestDTO<User> requestDTO){
        Wrapper wrapper  =new EntityWrapper();
        Page page = new Page(requestDTO.getPageNum(),requestDTO.getPageSize());
        wrapper.setSqlSelect("name as name","phone as phone");
        wrapper.eq("is_del",0);
        wrapper.in("org_id",Const.orgIds.get());
        String key  =requestDTO.getKey();
        if(StrUtil.isNotBlank(key)){
            wrapper.andNew().like("name",key).or().like("phone",key);
        }

        page=  userService.selectMapsPage(page,wrapper);
        return new ResultDTO(true,DataDTO.factory(page.getRecords(),page.getTotal()));
    }


}
