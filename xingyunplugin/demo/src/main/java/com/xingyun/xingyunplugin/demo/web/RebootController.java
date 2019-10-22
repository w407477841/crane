package com.xingyun.xingyunplugin.demo.web;

import com.xingyun.equipment.plugins.core.action.CommandAction;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:27 2019/7/12
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/")
@Slf4j
public class RebootController {
    @Autowired
    CommandAction commandAction;

    @GetMapping("reboot")
    public Object reboot(String sn){
        commandAction.reboot(sn);
        return "ok";
    }

}
