package com.xywg.equipment.sandplay.modular.sandplay.controller;

import com.xywg.equipment.sandplay.config.properties.Cmd;
import com.xywg.equipment.sandplay.core.util.rxtx.SerialTool;
import com.xywg.equipment.sandplay.core.vo.SerialVO;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:05 2018/10/8
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("weight")
public class WeightController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeightController.class);
@Autowired
    private Cmd cmd;
@GetMapping("send")
    public Object send(){

      SerialVO serialVO = SerialTool.serialMap.get(cmd.getWeightSerialPort());
        if(!serialVO.isOpen()){
            LOGGER.info("########################");
            LOGGER.info("########串口未开启####");
            LOGGER.info("########################");
        }else{

           SerialPort serialPort =  serialVO.getSerialPort();
            try {
                SerialTool.sendToPort(serialPort,"AT+F\r\n".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return "ok";
    }

}
