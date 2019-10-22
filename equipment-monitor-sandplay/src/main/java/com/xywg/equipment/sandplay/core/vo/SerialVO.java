package com.xywg.equipment.sandplay.core.vo;

import gnu.io.SerialPort;
import lombok.Data;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:34 2018/9/28
 * Modified By : wangyifei
 */
@Data
public class SerialVO {
    /**已开启*/
    private boolean    open;
    /**串口上下文*/
    private SerialPort   serialPort;
    /** 电子秤串口 */
    private boolean weightPort;


    /**
     *  非电子秤串口
     * @param open
     * @param serialPort
     * @return
     */
    public static SerialVO factoryNotWeight( boolean open,SerialPort serialPort){
        SerialVO serialVO  =new SerialVO();
        serialVO.setOpen(open);
        serialVO.setSerialPort(serialPort);
        serialVO.setWeightPort(false);
        return serialVO;
    }

    /**
     *  电子秤串口
     * @param open
     * @param serialPort
     * @return
     */
    public static SerialVO factoryWeight( boolean open,SerialPort serialPort){
        SerialVO serialVO  =new SerialVO();
        serialVO.setOpen(open);
        serialVO.setSerialPort(serialPort);
        serialVO.setWeightPort(true);
        return serialVO;
    }


}
