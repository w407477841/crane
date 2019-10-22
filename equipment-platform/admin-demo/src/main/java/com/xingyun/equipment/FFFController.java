package com.xingyun.equipment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:21 2019/7/25
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/")
public class FFFController {

@GetMapping("fff")
    public String fff(){
        return "ad";
    }

}
