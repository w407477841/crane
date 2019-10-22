package com.xywg.equipment.monitor.config;

import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.sjj.service.IProjectLiftService;
import com.xywg.equipment.monitor.modular.tj.service.IProjectCraneService;
import com.xywg.equipment.monitor.modular.yc.service.IProjectEnvironmentMonitorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 严鹏
 * @date 2019/8/2
 */
public class RedisInitConfig {

    @Autowired
    private IProjectEnvironmentMonitorService service;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private IProjectLiftService liftService;
    @Autowired
    private XywgProerties xywgProerties;
    @Autowired
    private IProjectCraneService craneService;

    public void start(){
        System.out.println("************扬尘转发设备查询开始*******************");
        selectMonitorDeviceNoOfNeedDispatch();
        System.out.println("************扬尘转发设备查询结束****************");

        System.out.println("************升降机转发设备查询开始*******************");
        selectLiftDeviceOfNeedDispatch();
        System.out.println("************升降机转发设备查询结束****************");

        System.out.println("************塔吊转发设备查询开始*******************");
        selectCraneDeviceOfNeedDispatch();
        System.out.println("************塔吊转发设备查询结束****************");

    }
    /**
     * 主动查询所有需要转发的扬尘设备 并进行缓存处理
     */
    private void selectMonitorDeviceNoOfNeedDispatch()
    {
        List<String> result=service.selectDeviceNoOfNeedDispatch();
        if(result!=null && result.size()>0)
        {
            //将设备从旧的缓存中删除
            for (int i=0;i<result.size();i++)
            {
                String s=result.get(i);
                System.out.println("设备号:"+s);
                if(redisUtil.get(xywgProerties.getRedisYcDispatchPrefix() + s)!=null)
                {
                    redisUtil.remove(xywgProerties.getRedisYcDispatchPrefix() + s);
                }
                redisUtil.set(xywgProerties.getRedisYcDispatchPrefix() + s,1);
            }

        }
    }

    /**
     * 主动查询所有需要转发的升降机设备 并进行缓存处理
     */
    private void selectLiftDeviceOfNeedDispatch()
    {
        List<String> result=liftService.selectDeviceNoOfNeedDispatch();
        if(result!=null && result.size()>0)
        {
            //将设备从旧的缓存中删除
            for (int i=0;i<result.size();i++)
            {
                String s=result.get(i);
                System.out.println("设备号:"+s);
                if(redisUtil.get(xywgProerties.getRedisSjjDispatchPrefix() + s)!=null)
                {
                    redisUtil.remove(xywgProerties.getRedisSjjDispatchPrefix() + s);
                }
                redisUtil.set(xywgProerties.getRedisSjjDispatchPrefix() + s,1);
            }

        }
    }

    /**
     * 主动查询所有需要转发的塔吊设备，并进行缓存处理
     */
    private  void selectCraneDeviceOfNeedDispatch()
    {
        List<String> result=craneService.selectDeviceNoOfNeedDispatch();
        if(result!=null && result.size()>0)
        {
            //将设备从旧的缓存中删除
            for (int i=0;i<result.size();i++)
            {
                String s=result.get(i);
                System.out.println("设备号:"+s);
                if(redisUtil.exists(xywgProerties.getRedisTdDispatchPrefix() + s))
                {
                    redisUtil.remove(xywgProerties.getRedisTdDispatchPrefix() + s);
                }
                redisUtil.set(xywgProerties.getRedisTdDispatchPrefix() + s,1);
            }
        }
    }
}
