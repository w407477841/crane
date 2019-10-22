package com.xywg.equipment.monitor.modular.dlt.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.config.properties.XywgProerties;
import com.xywg.equipment.monitor.core.util.RedisUtil;
import com.xywg.equipment.monitor.modular.dlt.dao.ProjectElectricPowerMapper;
import com.xywg.equipment.monitor.modular.dlt.model.ProjectElectricPower;
import com.xywg.equipment.monitor.modular.dlt.service.IProjectElectricPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
@Service
public class ProjectElectricPowerServiceImpl extends ServiceImpl<ProjectElectricPowerMapper, ProjectElectricPower> implements IProjectElectricPowerService {

    public final static String DEVICE_INFO_SUFF="str:deviceinfo:ameter";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XywgProerties xywgProerties;

    @Override
    public ProjectElectricPower getBaseInfo(String deviceNo) {

        String key = xywgProerties.getRedisHead() + ":" + DEVICE_INFO_SUFF+":"+deviceNo;
        if(redisUtil.exists(key)){
            String json = (String) redisUtil.get(key);
            return JSONUtil.toBean(json,ProjectElectricPower.class);
        }else{
            System.out.println("不存在"+key);
            Wrapper<ProjectElectricPower> wrapper = new EntityWrapper<>();
            wrapper.eq("device_no",deviceNo);
            wrapper.eq("is_del",0);
            ProjectElectricPower  result = null;
            List<ProjectElectricPower> projectElectricPowers = this.selectList(wrapper);
            if(projectElectricPowers == null || projectElectricPowers.size() == 0) {
                return null;
            }
            for(ProjectElectricPower projectWaterMeter : projectElectricPowers) {
                if(new Integer(1).equals(projectWaterMeter.getStatus())) {
                    result = projectWaterMeter;
                    break;
                }
            }
            if(result == null) {
                result = projectElectricPowers.get(projectElectricPowers.size() - 1);
            }
            //存5分钟
            redisUtil.set(key, JSONUtil.toJsonStr(result),5L);
            return result;
        }
    }

}
