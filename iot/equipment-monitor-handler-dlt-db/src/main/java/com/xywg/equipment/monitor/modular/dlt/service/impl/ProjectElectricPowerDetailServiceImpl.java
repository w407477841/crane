package com.xywg.equipment.monitor.modular.dlt.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.equipment.monitor.modular.dlt.dao.ProjectElectricPowerDetailMapper;
import com.xywg.equipment.monitor.modular.dlt.factory.BaseFactory;
import com.xywg.equipment.monitor.modular.dlt.model.ProjectElectricPowerDetail;
import com.xywg.equipment.monitor.modular.dlt.service.IProjectElectricPowerDetailService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yy
 * @since 2018-09-11
 */
@Service
public class ProjectElectricPowerDetailServiceImpl extends ServiceImpl<ProjectElectricPowerDetailMapper, ProjectElectricPowerDetail> implements IProjectElectricPowerDetailService {
    @Override
    public ProjectElectricPowerDetail getLastInfo(int eleId) {

        return baseMapper.getLastInfo(eleId);
    }

    /**
     * 同比增长率
     * @return
     */
    @Override
    public String getYearOnYear(){

        Date dateNow = new Date();

        Date yesDate = DateUtil.offsetDay(dateNow,-1);
        String date1 = DateUtil.format(yesDate,"yyyy-MM-dd");

        Date yes2Date = DateUtil.offsetDay(dateNow,-31);
        String date2 = DateUtil.format(yes2Date,"yyyy-MM-dd");

        System.out.println(date1+"   "+date2);
        BigDecimal first = baseMapper.getYearOnYear(BaseFactory.getTableName(ProjectElectricPowerDetail.class,yesDate),date1);
        System.out.println("#####################################################");
        System.out.println("first:"+first);


        BigDecimal sec = baseMapper.getYearOnYear(BaseFactory.getTableName(ProjectElectricPowerDetail.class,yes2Date),date2);

        System.out.println("sec:"+first.divide(sec,4,ROUND_HALF_UP).subtract(new BigDecimal(1)));

        BigDecimal result = first.divide(sec,4,ROUND_HALF_UP).subtract(new BigDecimal(1)).multiply(new BigDecimal(100)).setScale(2);

        return result.toString()+"%";
    }

    @Override
    public String getRingRatio(){

        Date date = new Date();

        Date yesDate = DateUtil.offsetMonth(date,-1);
        String date1 = DateUtil.format(yesDate,"yyyy-MM");

        Date yes2Date = DateUtil.offsetMonth(date,-2);
        String date2 = DateUtil.format(yesDate,"yyyy-MM");

        BigDecimal first = baseMapper.getRingRatio(BaseFactory.getTableName(ProjectElectricPowerDetail.class,yesDate),date1);

        BigDecimal sec = baseMapper.getRingRatio(BaseFactory.getTableName(ProjectElectricPowerDetail.class,yes2Date),date2);

        BigDecimal result = first.divide(sec,4,ROUND_HALF_UP).subtract(new BigDecimal(1)).multiply(new BigDecimal(100)).setScale(2);

        return result.toString()+"%";
    }

    public static void main(String[] args){
        BigDecimal a = new BigDecimal("1.23");
        BigDecimal b = new BigDecimal("2.23");
        BigDecimal cc = a.divide(b,4,ROUND_HALF_UP).subtract(new BigDecimal(1)).multiply(new BigDecimal(100)).setScale(2);

        System.out.println(cc.toString()+"%");
    }

}
