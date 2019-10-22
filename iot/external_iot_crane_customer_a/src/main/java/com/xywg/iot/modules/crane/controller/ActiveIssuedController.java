package com.xywg.iot.modules.crane.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hjy
 * @date 2018/10/8
 *  示例demo
 */
@RestController
@RequestMapping("/ssdevice/activeIssued")
public class ActiveIssuedController {
/*    @Autowired
    private ProjectHelmetAlarmService projectCraneService;*/


    /**
     * 发送升级指令 功能码0005
     *//*
    @PostMapping("/upgradePackage")
    public String upgradePackage() {
        return  projectCraneService.selectById(1).getDeviceNo();
    }*/

/*

    //http://localhost:8080/json
    @GetMapping(value = "/json",produces = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("all")
    public  Map<String,Object> index(){
        CeShiUser user1 = new CeShiUser("dalaoyang1", "26", "北京1");
        CeShiUser user2 = new CeShiUser("dalaoyang2", "22", "北京2");
        Map<String,Object>  map=new HashMap<>();
        List<CeShiUser> list= new ArrayList<>();
        list.add(user1);
        list.add(user2);
        map.put("DataTable",list);
        return map;
    }


    //http://localhost:8080/xml
    @SuppressWarnings("all")
    @GetMapping(value = "/xml",produces = MediaType.APPLICATION_XML_VALUE)
    public CeShiDataTable XML(){
        CeShiUser user1 = new CeShiUser("SSSS", "26", "北京1");
        CeShiUser user2 = new CeShiUser();
        user2.setUserAddress("");
        Map<String,Object>  map=new HashMap<>();
        List<CeShiUser> list= new ArrayList<>();
        list.add(user1);
        list.add(user2);
        //map.put("DataTable",list);
        CeShiGzzhxxs gzzhxxs  = new CeShiGzzhxxs();
        gzzhxxs.setUser(list);
        CeShiDataTable dataTable=new CeShiDataTable();
        dataTable.setGzzhxxs(gzzhxxs);
        return dataTable;
    }
*/


}



