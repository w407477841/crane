package com.xingyun.equipment.admin.modular.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 模块中文名称
 * @author wangcw
 * @description 如果有描述你就写,没有就算了
 * @date 7/8/18
 */
@RestController
@Api("这是一个美丽的模块控制层")
public class DemoMasterController {
    @RequestMapping(name="addPostDemoName",value = "/addPostDemo",method = RequestMethod.GET)
    @ApiOperation("这是一个POST方法")
    public String addPostDemo(@RequestBody Map accountVo) {
        return "OK";
    }

    @RequestMapping(value = "/getInfoDemo", method = RequestMethod.GET)
    @ApiOperation(value = "这是一个GET方法")
    public String getInfoDemo(Integer userId,String name) {
        return "OK";
    }
    @PreAuthorize(value="hasRole('USER:ADD')")
    @RequestMapping("/permission")
    public Object permissionTest(){
    	
    	return "OK";
    }
    @PreAuthorize(value="hasRole('USER:DEL')")
    @RequestMapping("/permission1")
    public Object permissionTest1(){
    	
    	return "OK";
    }
    
}
