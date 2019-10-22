package com.xywg.equipment.sandplay.modular.sandplay.controller;

import com.xywg.equipment.sandplay.core.util.rxtx.RxtxExport;
import com.xywg.equipment.sandplay.core.util.rxtx.SerialTool;
import com.xywg.equipment.sandplay.core.vo.SerialVO;
import gnu.io.SerialPort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @Auther: SJ
 * @Date: 2018/9/26 14:51
 * @Description:
 */
@Controller
@RequestMapping("/comTrans")
@CrossOrigin
public class ComTransController  extends BaseController{

    @RequestMapping("/getCom")
    @ResponseBody
    public Map getCom(){

        RxtxExport re = new RxtxExport();
        re.getCom();

        Map<String,List> map = new HashMap<String,List>();
        map.put("data",SerialTool.commListMap.get("test1"));

        return map;
    }

    @RequestMapping("/openCom")
    @ResponseBody
    public String openCom(String comStr,String bpsStr){
        RxtxExport re = new RxtxExport();
        try{
            SerialPort sp = re.openCom(comStr,bpsStr);

            if(SerialTool.serialMap.get(comStr)==null){
                SerialVO  sVo = SerialVO.factoryNotWeight(true,sp);
                sVo.setOpen(true);
                sVo.setSerialPort(sp);
                SerialTool.serialMap.put(comStr,sVo);
            }

            return "success";
        }catch(Exception e){
            return "failed";
        }
    }

    @RequestMapping("/sendMsg")
    @ResponseBody
    public String sendMsg(String type,String index){
        RxtxExport re = new RxtxExport();
        try{
            String msg = getSendData(type,Integer.parseInt(index));
            re.sendMsg( SerialTool.comm ,msg);
            return "success";
        }catch(Exception e){
            return "failed";
        }
    }

    @RequestMapping("/closeCom")
    @ResponseBody
    public String closeCom(){
        RxtxExport re = new RxtxExport();
        try{
            re.closePort(SerialTool.comm );
            return "success";
        }catch(Exception e){
            return "failed";
        }
    }

}
