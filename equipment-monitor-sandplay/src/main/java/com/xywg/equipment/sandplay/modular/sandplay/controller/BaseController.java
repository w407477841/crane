package com.xywg.equipment.sandplay.modular.sandplay.controller;

import com.xywg.equipment.sandplay.config.properties.BaseCmd;
import com.xywg.equipment.sandplay.config.properties.Cmd;
import com.xywg.equipment.sandplay.core.enums.DeviceType;
import com.xywg.equipment.sandplay.core.util.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:33 2018/9/26
 * Modified By : wangyifei
 */
public class BaseController {
    @Autowired
    protected Cmd cmd;

    private static final String FULL = "0" ;

    /**
     *   组装 发送数据
     * @param type
     * @param index
     * @return
     */
    protected   String getSendData(String type,Integer index){

        BaseCmd baseCmd =  ApplicationContextProvider.getBean(DeviceType.getByType(type).getClazz());

        String  sendData  =  cmd.getHead()+getData(baseCmd,index);



        return sendData;

    }
    /**
     * 获取偶校验CS的值
     * @return
     */
    private String getCS(String str){
        int sum = 0;
        for (int i = 0; i < str.length()/2; i++) {
            sum += Integer.parseInt(str.substring(i*2,(i+1)*2),16)%256;
        }
        String kou = Integer.toHexString(sum);
        return kou.substring(kou.length()-2,kou.length());
    }

    /**
     *
     * 根据
     * @param index
     * @return
     */
    private String getData(BaseCmd cmd,int index){
        List<String> addr = cmd.getAddr();
        List<String> control = cmd.getControl();
        List<String>  data  = cmd.getData();
        List<String> crc = cmd.getCrc();
        String sendData = full(2,addr.get(index)) +full(2,control.get(index))+full(8,data.get(index))+full(2,crc.get(index));
        return sendData;

    }

    private String full(int length,String data){
        int dataLength = data.length();
        if(dataLength<length){

            for(int i=0;i<length-dataLength;i++){
                data = FULL+data;
            }

        }
        return data;

    }

}
