package com.xywg.equipment.monitor.modular.whf.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.whf.dao.ProjectCraneMapper;
import com.xywg.equipment.monitor.modular.whf.model.ProjectCrane;
import com.xywg.equipment.monitor.modular.whf.service.IProjectCraneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wyf
 * @since 2018-08-20
 */
@Service
public class ProjectCraneServiceImpl extends ServiceImpl<ProjectCraneMapper, ProjectCrane> implements IProjectCraneService {
    private final  String ALL_STR_SUFF = "device:all";
    public final static String DEVICE_INFO_SUFF ="str:deviceinfo:crane";
    private static  final Logger LOGGER =LoggerFactory.getLogger(ProjectCraneServiceImpl.class);
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XywgProerties xywgProerties;

    @Override
    public List<Map<String,String>> selectAllDevice() {
        String key = xywgProerties.getRedisHead() + ":" + this.ALL_STR_SUFF;
        if (redisUtil.exists(key)) {
            System.out.println("#####从Redis读取了所有设备信息");
            return (List<Map<String,String>>) redisUtil.get(key);
        } else {
            System.out.println("#####从MySQL读取了所有设备信息");
            List<Map<String,String>> result = baseMapper.selectAllDevice();
            redisUtil.set(
                    key,
                    result,
                    5L
            );
            return result;
        }
    }
    @Override
    public ProjectCrane selectOne(String deviceNo) {
        String key = xywgProerties.getRedisHead() + ":" + ProjectCraneServiceImpl.DEVICE_INFO_SUFF+":"+deviceNo;
        if(redisUtil.exists(key)){
            LOGGER.debug("存在缓存: <"+key+">");
            String json = (String)redisUtil.get(key);
            return JSONUtil.toBean(json,ProjectCrane.class);
        }else{

            LOGGER.debug("不存在缓存: <"+key+">");
            Wrapper<ProjectCrane> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no",deviceNo);
            wrapper.eq("is_del",0);

            ProjectCrane  result = null;
            //查询所有该设备号的设备
            List<ProjectCrane> list= this.selectList(wrapper);
            if(list == null||list.size()==0){
                return null;
            }
            //找到启用的设备
            for(ProjectCrane monitor:list){
                if(new Integer(1).equals(monitor.getStatus())){
                    result = monitor;
                    break;
                }
            }
            //未找到启用设备，设备最新的一条为当前设备
            if(result==null){
                result = list.get(list.size()-1);
            }
            //存5分钟
            redisUtil.set(key,JSONUtil.toJsonStr(result),5L);
            return result;
        }



    }

    /**
     * 改变在线状态
     * @param tableName
     * @param deviceNo
     * @param status
     */
    @Override
    public void doStatusChange(String tableName,String deviceNo,int status){
        baseMapper.doDeviceOnlineChange(tableName,deviceNo,status);
    }

}
