package com.xywg.equipment.monitor.modular.handle;


import com.xywg.equipment.monitor.modular.disaptch.dao.DispatchMapper;
import com.xywg.equipment.monitor.modular.disaptch.model.Dispatch;
import com.xywg.equipment.monitor.netty.NettyClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author: wangyifei
* Description:
* Date: 16:45 2018/10/25
*/
@Component
public class DispatchService {
    @Autowired
    private DispatchMapper mapper;
    public void dispatch(String msg) {
        System.out.println("zbus 收到消息 : " + msg);
        // TODO 转义
     //   NettyClientService.sendMsg(dataToStr(msg));


        NettyClientService.sendMsg(msg);

        //转发内容入库
        Dispatch dispatch=new Dispatch();
        dispatch.setCreateTime(new Date());
        dispatch.setContent(msg);
        Integer count= mapper.insert(dispatch);
        if(count>0)
        {
            System.out.println("转发内容成功入库!");
        }
        else
        {
            System.out.println("转发入库失败!");
        }
    }


    private String dataToStr(String detail) {
        String[] datas = detail.split(",");
        Map<String,String> resMap = new HashMap<>();
        for(String data : datas){
            String[] kv = data.split(":");
            resMap.put(kv[0],kv[1]);
        }
        StringBuffer sb = new StringBuffer();
        sb.append("sdsyr:").append(resMap.get("deviceNo"))
                .append(",a:").append(resMap.get("pm25"))
                .append(",b:").append(resMap.get("pm10"))
                .append(",c:").append(resMap.get("temperature"))
                .append(",d:").append(resMap.get("humidity"))
                .append(",e:").append(resMap.get("noise"))
                .append(",f:").append("0")
                .append(",g:").append("0")
                .append(",h:").append("0")
                .append(",i:").append("0")
                .append(",j:").append(resMap.get("windSpeed"))
                .append(",k:").append(resMap.get("windDirection"))
                .append(",l:").append("0")
                .append(",m:").append("0");
        return sb.toString();
    }

}
